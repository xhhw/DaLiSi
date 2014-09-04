package com.zb.dalisi.test;

import com.meetup.memcached.MemcachedClient;
import com.meetup.memcached.SockIOPool;

public class MemcachedTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

//		BasicConfigurator.configure();
		String[] servers = { "127.0.0.1:11211" };
		SockIOPool pool = SockIOPool.getInstance();
		pool.setServers( servers );
		pool.setFailover( true );
		pool.setInitConn( 10 ); 
		pool.setMinConn( 5 );
		pool.setMaxConn( 250 );
		pool.setMaintSleep( 30 );
		pool.setNagle( false );
		pool.setSocketTO( 3000 );
		pool.setAliveCheck( true );
		pool.initialize();

		MemcachedClient mcc = new MemcachedClient();

		// turn off most memcached client logging:
		com.meetup.memcached.Logger.getLogger( MemcachedClient.class.getName() ).setLevel( com.meetup.memcached.Logger.LEVEL_WARN );

		for ( int i = 0; i < 10; i++ ) {
			boolean success = mcc.set( "" + i, "Hello!" );
			String result = (String)mcc.get( "" + i );
			System.out.println( String.format( "set( %d ): %s", i, success ) );
			System.out.println( String.format( "get( %d ): %s", i, result ) );
		}

		System.out.println( "\n\t -- sleeping --\n" );
		try { Thread.sleep( 10000 ); } catch ( Exception ex ) { }

		for ( int i = 0; i < 10; i++ ) {
			boolean success = mcc.set( "" + i, "Hello World!" );
			String result = (String)mcc.get( "" + i );
			System.out.println( String.format( "set( %d ): %s", i, success ) );
			System.out.println( String.format( "get( %d ): %s", i, result ) );
		}

	}

}
