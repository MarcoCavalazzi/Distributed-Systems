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
import java.util.StringTokenizer;

import jade.core.Agent;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;


public class CentralAgent extends Agent {

	@Override
	protected void setup() {
		
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
		sd.setType("central");
		sd.setName("Central Agent");
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
	
		log(" Taking down myself\n");
		
		try {
			log(" De-registering myself from the default DF...");
			DFService.deregister(this);
		} catch (FIPAException fe) {
			fe.printStackTrace();
		}
		log(" I'm done.");
	}
	
	
	/*
	 * Defining the Behaviour that sends and reads the messages
	 */
	private class MessageExaminerBehaviour extends Behaviour{
	
		// Creating Message Templates to manage correctly the ACL Messages
		/* 
		 * Template for user-defined messages 
		 */
		private MessageTemplate userTemplate = MessageTemplate.and(
												MessageTemplate.MatchPerformative(ACLMessage.REQUEST),
												MessageTemplate.MatchOntology("user") );

		/*
		 * Template for wheel-defined messages
		 */
		private MessageTemplate wheelTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology("wheel") );

		/*
		 * Template for ground-defined messages
		 */
		private MessageTemplate groundTemplate = MessageTemplate.and(
													MessageTemplate.MatchPerformative(ACLMessage.INFORM),
													MessageTemplate.MatchOntology("ground") );

		// Variables necessary for the exchange of ACL messages
		ACLMessage msg;
		ACLMessage reply = new ACLMessage(ACLMessage.REQUEST);  // setting the Performative is mandatory
		AID groundAgentAID=null, 
			batteryAgentAID=null,
			wheel1AgentAID=null,
			wheel2AgentAID=null,
			wheel3AgentAID=null,
			wheel4AgentAID=null,
			wheel5AgentAID=null,
			wheel6AgentAID=null;
		boolean foundAgentsFlag = false;
		
		@Override
		public void action() {
			log("In action.");
			
			// Finding the Ground Agent's AID. This operation is done only once at the beginning.
			if(!foundAgentsFlag)
			{
				// Starting the search for the other agents through the DF
				findAgents();
				
				// The search for agents of the system has been successful -> setting the flag to 'true'
				foundAgentsFlag = true;
				log("** Agenti trovati. -> Il sistema puo' partire.");
			}
			
			
			
			/*********************
			 * USER
			 *********************/
			// Attempting to receive a message from the User
			msg = myAgent.receive(userTemplate);
			// If a message from the User is fuond...
			if(msg!=null){
				log("Received REQUEST from '" + msg.getSender().getName() + "'.");
				
				// Creating the reply
				reply.setPerformative(ACLMessage.REQUEST);
				reply.setOntology("central agent");
				// Setting the Sender
				reply.setSender(myAgent.getAID());
				log("settato il Sender : " + myAgent.getAID());
				// Setting the Receivers
				reply.addReceiver(msg.getSender());
				// The agent forward the "GO" message to all the agents of the system except itself
				//addReceiversToMessage(reply, groundAgentAID, batteryAgentAID, wheel1AgentAID, wheel2AgentAID, wheel3AgentAID, wheel4AgentAID, wheel5AgentAID, wheel6AgentAID);
				addReceiversToMessage(reply, groundAgentAID, batteryAgentAID);
				
				// Setting the Content of the reply accordingly to what the User Requested
				if("GO".equals(msg.getContent()))
				{
					log("received User message content = " + msg.getContent());
					reply.setContent("GO");
					
					myAgent.send(reply);
				}
				else
				{
					if("STOP".equals(msg.getContent()))
					{
						log("received User message content = " + msg.getContent());
						reply.setContent("STOP");
						
						myAgent.send(reply);
					}
				}
				
				
			}
			
			
			/*********************
			 * WHEELS
			 *********************/
			// Attempting to receive a message from the Wheels
			msg = myAgent.receive(wheelTemplate);
			// If a message from the Wheels is fuond...
			if(msg!=null){
				log("Received INFORM from '" + msg.getSender().getName() + "'.");
				log("- Wheel message content = " + msg.getContent() + "\n");
				
				// Creating the reply
				//ACLMessage reply = msg.createReply();
				
			}
			
			
			/*********************
			 * GROUND
			 *********************/
			// Attempting to receive a message from the Ground
			msg = myAgent.receive(groundTemplate);
			// If a message from the Ground is found...
			if(msg!=null){
				log("Received INFORM from '" + msg.getSender().getName() + "'.");
				log("- Ground message content = " + msg.getContent() + "\n");
				// Creating the reply
				//ACLMessage reply = msg.createReply();
				
				
			}
			/*
			 * Blocking this behaviour until next message.
			 */
			log("Waiting for messages...");
			block();
			
		}
		
		
		/*
		 * findAgents()
		 * This function finds the AIDs of every other agent necessary to run the system and puts them in the correct variables.
		 * These variables are usable in the action() method.
		 */
		private void findAgents(){
			groundAgentAID = getAgentAID("Ground Agent");
			batteryAgentAID = getAgentAID("Battery Agent");
			/*
			wheel1AgentAID = getAgentAID("Wheel Agent 1");
			wheel2AgentAID = getAgentAID("Wheel Agent 2");
			wheel3AgentAID = getAgentAID("Wheel Agent 3");
			wheel4AgentAID = getAgentAID("Wheel Agent 4");
			wheel5AgentAID = getAgentAID("Wheel Agent 5");
			wheel6AgentAID = getAgentAID("Wheel Agent 6");
			*/
		}
		
		
		private void addReceiversToMessage(ACLMessage msg, AID groundAgentAID, AID batteryAgentAID){
			msg.addReceiver(groundAgentAID);
			msg.addReceiver(batteryAgentAID);
		}
		
		
		public AID getAgentAID(String name){
			// internal variable
			DFAgentDescription[] res = null;
			
			/*
			 * Search for the Ground Agent's AID 
			 */
			// Costruzione di un dfd per la ricerca selettiva
			DFAgentDescription dfd = new DFAgentDescription();
			ServiceDescription sd  = new ServiceDescription();
			sd.setName(name);
			dfd.addServices(sd);
			try{
				res = DFService.search(myAgent, dfd);
			}
			catch (FIPAException fe){
					fe.printStackTrace();
			}
			
			log("res 0 AID is = " + res[0].getName());
		
			// Restituisce l'AID dell'agente trovato usando la funzione getName()
			return res[0].getName();
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
	
	
	
	
	