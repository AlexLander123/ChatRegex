package me.alexlander123.chatregex;

public class CommandEntry{
	
	private String command;
	private int delay;
	
	public CommandEntry(String command, int delay) {
		this.command = command;
		this.delay = delay;
	}
	
	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public int getDelay() {
		return delay;
	}

	public void setDelay(int delay) {
		this.delay = delay;
	}

}
