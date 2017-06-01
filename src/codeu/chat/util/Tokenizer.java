package codeu.chat.util;
import java.io.IOException;

public final class Tokenizer{
	private StringBuilder token;
	private String source;
	private int at;

	public Tokenizer(String source){
		this.source = source;
	}

	private int remaining() {  
		return source.length() - at;
	}

	private char peek() throws IOException {  
		if(at < source.length()){
			return source.charAt(at);
		}
		else{
			//throw an exception
			throw new IOException("peek did not work");
		}
	}
	
	private char read() throws IOException { 
		final char c = peek();
		at += 1;
		return c;
	}

	public String next() throws IOException{
		//skip all leading whitespace
		while(remaining() > 0 && Character.isWhitespace(peek())){
			read(); //ignore the result because we already know that it is a whitespace character
		}
		if(remaining() <= 0){
			return null;
		}
		else if(peek() == '"'){
			 return readWithQuotes();
			//read a token that is surrounded by quotes
		}
		else{
			 return readWithNoQuotes();
			//read a token that is not surrounded by quotes
		}
	}

	private String readWithNoQuotes() throws IOException {
  		token.setLength(0);  // clear the token
 		 while (remaining() > 0 && !Character.isWhitespace(peek())) {
    	token.append(read());
  		}
  		return token.toString();
	}

	private String readWithQuotes() throws IOException {
  		token.setLength(0);  // clear the token
  		if (read() != '"') {
    		throw new IOException("Strings must start with opening quote");
 	 	}
  		while (peek() != '"') {
    		token.append(read());
  		}
  		read(); // read the closing the quote that allowed us to exit the loop
  		return token.toString();
	}


	
} //end class