public class Message {
	String command;
	  String data;

	  public Message(String message){
	    command = message.substring(0, 3);
	    data = message.substring(4);
	  }

	  public Message(String command, String data){
	    this.command = command;
	    this.data = data;
	  }

	  public String sendForm(){
	    return command + " " + data;
	  }
}
