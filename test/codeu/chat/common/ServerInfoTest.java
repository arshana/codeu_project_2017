package codeu.chat.common;

import static org.junit.Assert.*;

import org.junit.Test;

import codeu.chat.util.Time;
import codeu.chat.util.Uuid;

public class ServerInfoTest {

	@Test
	public void testEquals() {
	    final ServerInfo s1 = new ServerInfo();
	    final ServerInfo s2 = new ServerInfo();
	    assertTrue(s1.equals(s1));
	    assertTrue(s2.equals(s2));
	    assertTrue(s1.equals(s2));
	    assertTrue(s2.equals(s1));
	}
	
	@Test
	public void testEqualsWithParameters() {
		final Uuid u1 = new Uuid(5);
	    final ServerInfo s1 = new ServerInfo(u1, Time.now());
	    final ServerInfo s2 = new ServerInfo(u1, Time.now());
	    assertTrue(s1.equals(s1));
	    assertTrue(s2.equals(s2));
	    assertTrue(s1.equals(s2));
	    assertTrue(s2.equals(s1));
	}
	
	public void testNotEqualsWithParameters() {
		final Uuid u1 = new Uuid(5);
		final Uuid u2 = new Uuid(7);
	    final ServerInfo s1 = new ServerInfo(u1, Time.now());
	    final ServerInfo s2 = new ServerInfo(u2, Time.now());
	    assertTrue(s1.equals(s1));
	    assertTrue(s2.equals(s2));
	    assertTrue(s1.equals(s2));
	    assertTrue(s2.equals(s1));
	}

}
