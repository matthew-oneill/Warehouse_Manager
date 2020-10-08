package classes;

public interface Data{
	// interface used for common methods across different objects
	// utilises polymorphism to reduce code length
	public String convertToCommaSeparatedString();
	public String convertToFormattedString();
	public String getID();
	
}