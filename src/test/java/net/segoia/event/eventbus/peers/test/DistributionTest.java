/**
 * event-bus - An event bus framework for event driven programming
 * Copyright (C) 2016  Adrian Cristian Ionescu - https://github.com/acionescu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.segoia.event.eventbus.peers.test;

import org.junit.Ignore;
import org.junit.Test;

import junit.framework.Assert;
import net.segoia.event.conditions.StrictEventMatchCondition;
import net.segoia.event.conditions.TrueCondition;
import net.segoia.event.eventbus.Event;
import net.segoia.event.eventbus.FilteringEventBus;
import net.segoia.event.eventbus.constants.Events;
import net.segoia.event.eventbus.peers.LocalEventBusNode;
import net.segoia.event.eventbus.test.TestEventListener;
import net.segoia.event.eventbus.util.EBus;

public class DistributionTest {

    
    @Test
    @Ignore
    public void simpleDistributionTest() {
	/**
	 * Create two buses, peer them together, then see if events reach out from one to the other
	 */
	
	FilteringEventBus b1 = new FilteringEventBus();
	FilteringEventBus b2 = new FilteringEventBus();
	
	LocalEventBusNode node1 = new LocalEventBusNode(b1);
	LocalEventBusNode node2 = new LocalEventBusNode(b2);
	
	LocalEventBusNode mainNode = new LocalEventBusNode(EBus.instance());
	
	/* register main node on all events */
	mainNode.registerPeerAsAgent(node1, new TrueCondition());
	mainNode.registerPeerAsAgent(node2 , new TrueCondition());
	
	node1.registerPeer(node2);
	
	TestEventListener tl1 = new TestEventListener();
	b1.registerListener(tl1);
	
	TestEventListener tl2 = new TestEventListener();
	b2.registerListener(tl2);
	
	Event e1 = Events.builder().system().message().name("test").build();
	
	b1.postEvent(e1);
	
	Assert.assertTrue(tl1.hasReceivedEvent(e1));
	
	Assert.assertFalse(tl2.hasReceivedEvent(e1));
	
	
	/* register node2 for the type of event e1 */
	StrictEventMatchCondition tec = new StrictEventMatchCondition(e1.getEt());
	node1.registerPeerAsAgent(node2,tec);
	
	Event e2 = Events.builder().system().message().name("test").build();
	
	b1.postEvent(e2);
	
	try {
	    Thread.sleep(1000);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	/* now we should receive the event */
	Assert.assertTrue(tl2.hasReceivedEvent(e2));
	
	Event e3 = Events.builder().system().message().name("test").build();
	
	b2.postEvent(e3);
	
	Assert.assertFalse(tl1.hasReceivedEvent(e3));
	
	node2.registerPeer(node1, tec);
	
	b2.postEvent(e3);
	
	Assert.assertTrue(tl1.hasReceivedEvent(e3));
	
    }
    
}
