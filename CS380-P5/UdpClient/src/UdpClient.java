import java.io.BufferedReader;
import java.nio.ByteBuffer;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Arrays;

public class UdpClient {

	public static void main(String[] args) {
		 UdpClient udpc = new UdpClient();
	        try {
	            Socket socket = new Socket("codebank.xyz", 38005);
	            BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	            //I attempted to get this program to work for several days but no matter what I can not find whats wrong with part 1
	            //Part 1 of this project needs to be done to even attempt part 2 and 3 but despite it just havinf to be a
	            //IPv4 packet from project 3 and my project working the response from the sever says its wrong
	            //so here is my attempt at the project so far...
	 
	            // Header fields
	            short versHlenTos = 0x4500;
	            short len;
	            short ident = 0;
	            short flagOff = 0x4000; // no fragmentation
	            byte ttl = 50;
	            byte protocol = 6; // TCP=6, udp =17
	            short checksum;// header only
	            int sourAdd = 0; // IP address of my choice
	            byte[] destAdd = socket.getInetAddress().getAddress();
	            String dat = "DEADBEEF";
	            byte[] data = new byte[]{-34,-83,-66,-17};

	 
	            int dataSize = 4;
	            
 
                len = (short) (20 + dataSize);	 
       
                checksum = 0;
                ByteBuffer shake = ByteBuffer.allocate(20 + dataSize);
                shake.putShort(versHlenTos);
                shake.putShort(len);
                shake.putShort(ident);
                shake.putShort(flagOff);
                shake.put(ttl);
                shake.put(protocol);
                shake.putShort(checksum);
                shake.putInt(sourAdd);
                shake.put(destAdd);
                shake.put(data);
 
                checksum = udpc.checksum(shake.array());
 
                // Create Packet
                shake.clear();
                shake.putShort(versHlenTos);
                shake.putShort(len);
                shake.putShort(ident);
                shake.putShort(flagOff);
                shake.put(ttl);
                shake.put(protocol);
                shake.putShort(checksum);
                shake.putInt(sourAdd);
                shake.put(destAdd);
                shake.put(data);
               
                socket.getOutputStream().write(shake.array());
                System.out.print("Handshake Response: ");
                int code = br.read();
                int code1 = br.read();
                int code2 = br.read();
                int code3 = br.read();
                //int port = br.read();
                System.out.println(code + ","+ code1 + ","+code2 + ","+ code3 );
               
                System.out.println( "The above numbers translate to BAAD FOOD,\n"
                		+ "and no matter that I do I cant fix this issue so I cant get farther.\n"
                		+ "If my IPv4 packets in Project 3 where correct why is this one not?");
                
                dataSize = 2;
	            
	            for (int i = 0; i < 12; ++i) {
	                System.out.println("Sending packet with " + dataSize + " bytes of data");
	                data = udpc.fillDat(dataSize);
	 
	                len = (short) (20 + dataSize);	 
	       
	                checksum = 0;
	                ByteBuffer bbuf = ByteBuffer.allocate(20 + dataSize);
	                bbuf.putShort(versHlenTos);
	                bbuf.putShort(len);
	                bbuf.putShort(ident);
	                bbuf.putShort(flagOff);
	                bbuf.put(ttl);
	                bbuf.put(protocol);
	                bbuf.putShort(checksum);
	                bbuf.putInt(sourAdd);
	                bbuf.put(destAdd);
	                bbuf.put(data);
	 
	                checksum = udpc.checksum(bbuf.array());
	 
	                // Create Packet
	                bbuf.clear();
	                bbuf.putShort(versHlenTos);
	                bbuf.putShort(len);
	                bbuf.putShort(ident);
	                bbuf.putShort(flagOff);
	                bbuf.put(ttl);
	                bbuf.put(protocol);
	                bbuf.putShort(checksum);
	                bbuf.putInt(sourAdd);
	                bbuf.put(destAdd);
	                bbuf.put(data);
	                dataSize = dataSize * 2;
	 
	                socket.getOutputStream().write(bbuf.array());
	                System.out.println(br.readLine() + "\n");
	            }
	 
	            socket.close();
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	}
	public byte[] fillDat(int dataSize) {
		 
        byte[] data = new byte[dataSize];
 
        for (int i = 0; i < dataSize; i++) {
            data[i] = 0;
        }
 
        return data;
    }
	
	public short checksum(byte[] b) {
        int sum = 0;
        int length = b.length;
        int i = 0;
 
        while (length > 1) {
            int s = ((b[i++] << 8) & 0xFF00) | (b[i++] & 0x00FF);
            sum += s;
            if ((sum & 0xFFFF0000) > 0) {
                sum &= 0xFFFF;
                sum++;
            }
            length -= 2;
        }
 
        return (short) ~(sum & 0xFFFF);
    }
	public static byte[] hexStringToByteArray(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
}
