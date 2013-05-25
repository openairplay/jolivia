package org.dyndns.jkiddo;

import javax.jmdns.NetworkTopologyListener;
import javax.jmdns.ServiceListener;

public interface IDiscoverer extends ServiceListener, NetworkTopologyListener
{

}
