import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class WebServer {
    public static void main(String[] args) throws Exception{
        try(
        		ServerSocket sock = new ServerSocket(8080)){
        	while(true) {
        Socket socket = sock.accept();
        InputStream in = socket.getInputStream();
        BufferedReader bufR = new BufferedReader(new InputStreamReader(in));
        OutputStream out = socket.getOutputStream();
        PrintWriter pw = new PrintWriter(out, true);
            try {
                String input = bufR.readLine();
                String fileN = "www" + input.split(" ")[1];
                System.out.println(fileN);
                
                if (fileN.equals("www/hello.html")) {
                	File file = new File(fileN);
                    BufferedReader br = new BufferedReader(new FileReader(file));
                    pw.print("HTTP/1.1 200 OK \n "
                    		+ "Content-type: text/html \n "
                    		+ "Content-length: " + file.length());
                    input = br.readLine();
                    while (input != null) {
                        pw.print(input + "\n");
                        input = br.readLine();
                    }
                    br.close();
                    pw.close();
                } else {
                	File fail = new File("www/NotFound.html");
                    BufferedReader br = new BufferedReader(new FileReader(fail));
                    pw.print("HTTP/1.1 404 Not Found \n "
                    		+ "Content-type: text/html \n "
                    		+ "Content-length: " + fail.length());
                    input = br.readLine();
                    while (input != null) {
                        pw.print(input + "\n");
                        input = br.readLine();
                    }
                    br.close();
                    pw.flush();
                    pw.close();
                }
            } catch(Exception e) {
                System.out.println(e);
            }
        	}
        }catch(Exception e) {
            System.out.println("Socket error!");
        }
    }
}