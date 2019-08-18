
import java.io.*;
import java.net.*;
import java.util.*;

public class TicTacToeClient {
	
    static ObjectOutputStream send;
    static ObjectInputStream receive;
    static Scanner scan = new Scanner(System.in);
    static byte x,y;
    
    public static void main(String[] args){
        System.out.println("Connecting...");
        Socket socket;
        try{
            socket = new Socket("codebank.xyz", 38006);
            System.out.println("Success");

            receive = new ObjectInputStream(socket.getInputStream());
            send = new ObjectOutputStream(socket.getOutputStream());
            //sending players name
            ConnectMessage name = new ConnectMessage("Pablo Leon");
            send.writeObject(name);
            //Starting game
            System.out.println("Starting Tic-Tac-Toe");
            CommandMessage start = new CommandMessage(CommandMessage.Command.NEW_GAME);
            send.writeObject(start);

            while(true){
                System.out.println("Updating game state...");
                Message mess = (Message)receive.readObject();

                if(mess instanceof BoardMessage){
                    displayGame(((BoardMessage)mess).getBoard());
                    
                    placeMark();
                }
                else if(mess instanceof ErrorMessage){
                    System.out.println("Error: " + ((ErrorMessage)mess).getError());
                    System.exit(-1);
                }
                else{
                    System.out.println("Failure quitting game, please try again");
                    System.exit(-1);
                }
            }
        }
        catch(Exception e){
            System.out.println("Exception: " + e.toString());
        }
    }
    
    public static void displayGame(byte[][] board){
        for(int i = 0; i < board.length; i++){
            System.out.print("\t");
            for(int j = 0; j < board[i].length; j++){
                switch(board[i][j]){
                    case 0:
                        System.out.print("  .");
                        break;
                    case 1:
                        System.out.print("  X");
                        break;
                    case 2:
                        System.out.print("  O");
                        break;
                    default:
                        System.out.print("NotOpt");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void placeMark(){
        scan.useDelimiter("(\\n)|,");
        System.out.print("Choose where you'd like to place your mark: ");
        x = scan.nextByte();
        y = scan.nextByte();
        try{
            send.writeObject(new MoveMessage(y,x));
        }
        catch(Exception e){
            System.out.println("Exception: " + e.toString());
            System.exit(-1);
        }
    }

}