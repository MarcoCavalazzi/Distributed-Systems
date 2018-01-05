/*****************************************************************
JADE - Java Agent DEvelopment Framework is a framework to develop 
multi-agent systems in compliance with the FIPA specifications.
Copyright (C) 2000 CSELT S.p.A. 

GNU Lesser General Public License

This library is free software; you can redistribute it and/or
modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation, 
version 2.1 of the License. 

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
Lesser General Public License for more details.

You should have received a copy of the GNU Lesser General Public
License along with this library; if not, write to the
Free Software Foundation, Inc., 59 Temple Place - Suite 330,
Boston, MA  02111-1307, USA.
 *****************************************************************/

package bin.project;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.StringTokenizer;

import jade.core.Agent;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


public class GroundAgent extends Agent {

	@Override
	protected void setup() {
		
		// Debug statement
		log("Setup initiated.");
	
		/*
		 *  1- Create the agent description.
		 */
		DFAgentDescription dfd = new DFAgentDescription();
		/*
		 *  2- Fill its mandatory fields.
		 */
		dfd.setName(getAID());
		/*
		 *  3- Create the service description.
		 */
		ServiceDescription sd = new ServiceDescription();
		/*
		 *  4- Fill its mandatory fields.
		 */
		sd.setType("ground");
		sd.setName("Ground Agent");
		/*
		 *  5- Add the service description to the agent description.
		 */
		dfd.addServices(sd);
		try {
			/*
			 *  6- Register the service (through the agent description multiple
			 *  services can be registered in one shot).
			 */
			log("Registering '"+sd.getType()+"' service named '"+sd.getName()+"'" +
					"to the default DF...");
			DFService.register(this, dfd);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		
	
		/*
		 * Adding the behaviour that sends and reads the ACL Messages
		 */
		addBehaviour(new MessageExaminerBehaviour());
	}
		
		
	/*
	 * Deregistering the services offered by the agent upon shutdown,
	 * because the JADE platform does not do it by itself!
	 */
	@Override
	protected void takeDown() {
	
		log("\nTaking down myself\n");
		
		try {
			log("De-registering myself from the default DF...");
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		log("I'm done.");
	}
	
	
	/*
	 * Defining the Behaviour that sends and reads the messages
	 */
	private class MessageExaminerBehaviour extends Behaviour{
	
		// Creating Message Templates to manage correctly the ACL Messages
		/* 
		 * Template for user-defined messages 
		 */
		private MessageTemplate centralTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
													MessageTemplate.MatchOntology("central agent") );

		/*
		 * Template for wheel-defined messages
		 */
		private MessageTemplate wheelTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology("wheel agent") );

		ACLMessage msg;
		ACLMessage reply;
		
		@Override
		public void action() {
			
			log("In action.");
			
			/*********************
			 * CENTRAL AGENT
			 *********************/
			// Attempting to receive a message from the Central Agent
			msg = myAgent.receive(centralTemplate);
			
			// If a message from the Central Agent is fuond...
			if(msg!=null){
				log("Received REQUEST from '" + msg.getSender().getName() + "'.");
				
				// Creating the reply
				reply = msg.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				
				if("GO".equals(msg.getContent()))
				{
					log("message content = " + msg.getContent());
					reply.setContent("GO received");
					
					myAgent.send(reply);
					log("Reply sent");
				}
				else
				{
					if("STOP".equals(msg.getContent()))
					{
						log("message content = " + msg.getContent());
						reply.setContent("STOP received");
						
						myAgent.send(reply);
						log("Reply sent");
					}
				}
			}
			
			
			/*********************
			 * WHEEL AGENT
			 *********************/
			// Attempting to receive a message from the Central Agent
			msg = myAgent.receive(wheelTemplate);
			
			// If a message from the Central Agent is fuond...
			if(msg!=null){
				log("Received INFORM from '" + msg.getSender().getName() + "'.");
				log("\n--- Wheel message content = " + msg.getContent() + "\n");
				
				// Creating the reply
				ACLMessage reply = msg.createReply();
				
				
			}
			
			/*
			 * Blocking this behaviour until next message.
			 */
			log("Waiting for messages...");
			block();
			
		}
		
		
		@Override
		public boolean done() {
			// This behaviour is set to be cyclic.
			// this means that the method done() has to return always "false"
			return false;
		}
		
		@Override
		public int onEnd() {
			log("onEnd() method called. >> Terminating...");
			myAgent.doDelete();
			return super.onEnd();
		}
		
	}
	
	private void log(String msg) {
		System.out.println("["+getName()+"]: "+msg);
	}
	
}
	
	
	
	
	