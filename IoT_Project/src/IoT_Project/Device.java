package IoT_Project;

public class Device {
	/* byte mac[6]*/
	public byte[] mac = new byte[6];
	
	public int id;
		
	
	public void setMac(byte[] mac) {
		this.mac = mac;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	
	public byte[] getMac() {
		return mac;
	}
	
	public int getId() {
		return id;
	}
}
