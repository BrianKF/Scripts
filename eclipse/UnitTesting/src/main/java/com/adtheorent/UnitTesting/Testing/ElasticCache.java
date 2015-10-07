package com.adtheorent.UnitTesting.Testing;

import java.io.IOException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.FailureMode;
import net.spy.memcached.KetamaConnectionFactory;
import net.spy.memcached.MemcachedClient;
import net.spy.memcached.ConnectionFactoryBuilder.Protocol;

public class ElasticCache {
	
	private MemcachedClient memcachedClient = null;
	//private static String MEMCACHE_END_POINT_URL = 
	
	public ElasticCache() {
		final ConnectionFactoryBuilder cfb = new ConnectionFactoryBuilder( new KetamaConnectionFactory() );
		cfb.setProtocol( Protocol.BINARY );
		cfb.setFailureMode( FailureMode.Retry );
		//memcachedClient = new MemcachedClient( cfb.build(), AddrUtil.getAddresses( MEMCACHE_END_POINT_URL ) );
	}
}
