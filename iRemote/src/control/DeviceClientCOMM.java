package control;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.CommandListener;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Form;
import javax.microedition.lcdui.List;

/**
 * A class that demonstrates Bluetooth communication between client mode device
 * and server mode PC through serial port profile.
 */
public class DeviceClientCOMM implements DiscoveryListener {


	/*-
	 *  ---- Bluetooth attributes ----
	 */
	protected UUID uuid = new UUID(0x1101); // serial port profile

	protected int inquiryMode = DiscoveryAgent.GIAC; // no pairing is needed

	protected int connectionOptions = ServiceRecord.NOAUTHENTICATE_NOENCRYPT;

	/*-
	 *  ---- Echo loop attributes ----
	 */

	protected int stopToken = 255;

	/*-
	 *  ---- GUI attributes ----
	 */

	protected Form infoArea = new Form("Bluetooth Client");

	protected Vector deviceList = new Vector();

        StreamConnection stream ;
	InputStream in;
        OutputStream out;

	iRemote midlet;

	 DeviceClientCOMM(iRemote m)  {
           
             
             midlet=m;
		makeInformationAreaGUI();


			try {
				startDeviceInquiry();
			} catch (Throwable t) {
				log(t);
                        }
	}

	/*-
	 *   -------  Device inquiry section -------
	 */

	private void startDeviceInquiry() {
		try {
			log("Start inquiry method - this will take few seconds...");
			DiscoveryAgent agent = getAgent();
			agent.startInquiry(inquiryMode, this);
		} catch (Exception e) {
			log(e);
		}
	}

	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass cod) {
		log("A device discovered (" + getDeviceStr(btDevice) + ")");
		deviceList.addElement(btDevice);
	}

	public void inquiryCompleted(int discType) {
		log("Inquiry compeleted. Please select device from combo box.");
		makeDeviceSelectionGUI();
	}

	/*-
	 *   -------  Service search section -------
	 */

	private void startServiceSearch(RemoteDevice device) {
		try {
			log("Start search for Serial Port Profile service from "
					+ getDeviceStr(device));
			UUID uuids[] = new UUID[] { uuid };
			getAgent().searchServices(null, uuids, device, this);
		} catch (Exception e) {
			log(e);
		}
	}

	/**
	 * This method is called when a service(s) are discovered.This method starts
	 * a thread that handles the data exchange with the server.
	 */
	public void servicesDiscovered(int transId, ServiceRecord[] records) {
		log("Service discovered.");
		for (int i = 0; i < records.length; i++) {
			ServiceRecord rec = records[i];
			String url = rec.getConnectionURL(connectionOptions, false);
			handleConnection(url);
		}
	}

	public void serviceSearchCompleted(int transID, int respCode) {
		String msg = null;
		switch (respCode) {
		case SERVICE_SEARCH_COMPLETED:
			msg = "the service search completed normally";
			break;
		case SERVICE_SEARCH_TERMINATED:
			msg = "the service search request was cancelled by a call to DiscoveryAgent.cancelServiceSearch()";
			break;
		case SERVICE_SEARCH_ERROR:
			msg = "an error occurred while processing the request";
			break;
		case SERVICE_SEARCH_NO_RECORDS:
			msg = "no records were found during the service search";
			break;
		case SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
			msg = "the device specified in the search request could not be reached or the local device could not establish a connection to the remote device";
			break;
		}
		log("Service search completed - " + msg);
	}

	/*-
	 *   -------  The actual connection handling. -------
	 */

	private void handleConnection(final String url) {
		Thread echo = new Thread() {
			public void run() {
			 stream = null;
				try {
					log("Connecting to server by url: " + url);
					stream = (StreamConnection) Connector.open(url);

					log("Bluetooth stream open.");
					 in = stream.openInputStream();
					 out = stream.openOutputStream();
					log("Start echo loop.");

				Render r=new Render(midlet,1,DeviceClientCOMM.this);



				} catch (IOException e) {
					log(e);
				} finally {
					log("Bluetooth stream closed.");
					if (stream != null) {
						try {
							stream.close();
						} catch (IOException e) {
							log(e);
						}
					}
				}//finally
			}
		};
		echo.start();
	}

void WriteRead(int n)
{
    try{

out.write(n);
out.flush();
int r = in.read();
log("Read " + r + ", write it back.");
/*if (r == stopToken) {
log("Stop echo loop.");
break;*/
    }
    catch (IOException e) {
	log(e);
} finally {
    log("Bluetooth stream closed.");
if (stream != null) {
	try {
	stream.close();
	} catch (IOException e) {
	log(e);
         }//catch
}//if
}//finally
}//WriteRead
	/*-
	 *   -------  Graphic User Interface section -------
	 */

	private void makeInformationAreaGUI() {
		infoArea.deleteAll();
		midlet.display.setCurrent(infoArea);
	}

	private void makeDeviceSelectionGUI() {

final List devices = new List("Select a device", List.IMPLICIT);
	for (int i = 0; i < deviceList.size(); i++)
	devices.append(getDeviceStr((RemoteDevice) deviceList.elementAt(i)), null);

		devices.setCommandListener(new CommandListener() {
			public void commandAction(Command arg0, Displayable arg1) {
				makeInformationAreaGUI();
				startServiceSearch((RemoteDevice) deviceList.elementAt(devices
						.getSelectedIndex()));
			}
		});
		midlet.display.setCurrent(devices);
	}

	synchronized private void log(String msg) {
		infoArea.append(msg);
		infoArea.append("\n\n");
	}

	private void log(Throwable e) {
		log(e.getMessage());
	}

	/*-
	 *   -------  Utils section - contains utility functions -------
	 */

	private DiscoveryAgent getAgent() {
		try {
			return LocalDevice.getLocalDevice().getDiscoveryAgent();
		} catch (BluetoothStateException e) {
			throw new Error(e.getMessage());
		}
	}

	private String getDeviceStr(RemoteDevice btDevice) {
		return getFriendlyName(btDevice) + " - 0x"
				+ btDevice.getBluetoothAddress();
	}

	private String getFriendlyName(RemoteDevice btDevice) {
		try {
			return btDevice.getFriendlyName(false);
		} catch (IOException e) {
			return "no name available";
		}
	}

}
