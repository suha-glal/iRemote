using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Windows.Forms;
using InTheHand.Net;
using InTheHand.Net.Bluetooth;
using InTheHand.Net.Sockets;
using System.IO;
using System.Text;

namespace SensorPTTremote
{
    public partial class Form1 : Form
    {
        // This delegate enables asynchronous calls for setting
        // the text property on a TextBox control.
        delegate void SetTextCallback(string text);

        private BluetoothListener lsnr;
        private bool listening = true;

        public Form1()
        {
            InitializeComponent();
        }

        private void Form1_Load(object sender, EventArgs e)
        {
            textBox1.Text += "Waiting for clients connections....\n";
            lsnr = new BluetoothListener(BluetoothService.SerialPort);
            lsnr.Start();


            System.Threading.Thread t = new System.Threading.Thread(new System.Threading.ThreadStart(ListenLoop));
            t.Start();
        }//Form1_Load
        private void ListenLoop()
        {
            byte[] buffer = new byte[4];
            int received = 0;

            while (listening)
            {
                BluetoothClient bc;
                System.IO.Stream ns;
                try
                {
                    bc = lsnr.AcceptBluetoothClient();
                    ns = bc.GetStream();
                }
                catch
                {
                    break;
                }


                //keep connection open
                while (listening)
                {
                    try
                    {
                        byte[] wbuffer = StrToByteArray("hi");
                        ns.Write(wbuffer, 0, wbuffer.Length);

                        received = ns.Read(buffer, 0, buffer.Length);



                    }
                    catch
                    {
                        break;
                    }

                    if (received > 0)
                    {
                        //string command = "";

                        int keycode = (int)BitConverter.ToInt16(buffer, 0);
                        String s = textBox1.Text;
                        SetText(s + "\n" + keycode);


                    }
                    else
                    {
                        //connection lost
                        break;
                    }
                }

                try
                {
                    bc.Close();
                }
                catch
                {
                }
            }

        }//listenloop
        private void SetText(string text)
        {
            // InvokeRequired required compares the thread ID of the
            // calling thread to the thread ID of the creating thread.
            // If these threads are different, it returns true.
            if (this.textBox1.InvokeRequired)
            {
                SetTextCallback d = new SetTextCallback(SetText);
                this.Invoke(d, new object[] { text });
            }
            else
            {
                this.textBox1.Text = text;
            }
        }



        public static byte[] StrToByteArray(string str)
        {
            System.Text.ASCIIEncoding encoding = new System.Text.ASCIIEncoding();
            return encoding.GetBytes(str);
        }
        private void Form1_Closing(object sender, System.ComponentModel.CancelEventArgs e)
        {
            listening = false;
            lsnr.Stop();
        }//Form1_Closing
    }
}
