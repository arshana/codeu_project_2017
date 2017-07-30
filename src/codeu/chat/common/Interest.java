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
			Uuid.SERIALIZER.write(out, value.userid);
			Serializers.STRING.write(out, value.title);
			Serializers.STRING.write(out, value.type);
			
		}

		@Override
		public Interest read(InputStream in) throws IOException {
			return new Interest(
					Uuid.SERIALIZER.read(in),
					Uuid.SERIALIZER.read(in),
					Serializers.STRING.read(in),
					Serializers.STRING.read(in)
			);
		}
	};

	public final Uuid id;
	public final Uuid userid;
	public final String title;
	public final String type;

	public Interest(Uuid id, Uuid userid, String title, String type) {

	    this.id = id;
	    this.userid = userid;
	    this.type = type;
	    this.title = title;

	}
  
	@Override
	public boolean equals(Object obj){
		if (obj instanceof Interest){
			if (((Interest) obj).userid.equals(this.userid) &&
					((Interest) obj).title.equals(this.title) &&
					((Interest) obj).type.equals(this.type)){
				return true;
			}
		}
		return false;
	}
}
