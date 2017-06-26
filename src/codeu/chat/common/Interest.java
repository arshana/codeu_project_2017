package codeu.chat.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

import codeu.chat.util.Serializer;
import codeu.chat.util.Serializers;
import codeu.chat.util.Time;
import codeu.chat.util.Uuid;

public class Interest {
	
	  public static final Serializer<Interest> SERIALIZER = new Serializer<Interest>() {

		@Override
		public void write(OutputStream out, Interest value) throws IOException {
			 Uuid.SERIALIZER.write(out, value.id);
		      Serializers.STRING.write(out, value.title);
		      Serializers.STRING.write(out, value.type);
			
		}

		@Override
		public Interest read(InputStream in) throws IOException {
			return new Interest(
			          Uuid.SERIALIZER.read(in),
			          Serializers.STRING.read(in),
			          Serializers.STRING.read(in)
			      );
		}
	  };

	  public final Uuid id;
	  public final String title;
	  public final String type;

	  public Interest(Uuid id, String title, String type) {

	    this.id = id;
	    this.type = type;
	    this.title = title;

	  }
}
	
