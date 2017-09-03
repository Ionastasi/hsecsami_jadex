This file is machine translated. See `READMErus.md` for original russian version.

# AI-Cognitive: multiagent system Jadex
## Anastasia Iovleva

### Relevance
The interaction of systems / agents / robots is one of the key topics in the development of modern AI. Indeed, it is not enough to create robots that know how to perform their tasks, we still need to teach them how to work together with others, distribute tasks, make decisions based on what other elements of the system are doing.

To solve such problems, multi-agent modeling systems are used. They differ from the others in that the elements of the system (objects, agents) make decisions based on what other objects are doing, what information they have, and what their needs are. This situation is much closer to the present compared to systems where all objects have all the information, or their actions are coordinated by some single body. For example, imagine a column of cars on the track. The driver does not see the entire road, so he should be guided by the actions of the machine in front. Also, he must warn the surrounding cars about the actions that he must take. Accordingly, to create a model for such a vehicle-to-vehicle behavior, a multi-antenna system is needed. And, as you can understand, in many cases, when several objects / machines / robots / AI perform one task, while having different capabilities and limitations and not knowing full information about the world, multi-agent systems are applicable.

With one of these systems, Jadex, we will get acquainted in the framework of the project work.

### Technologies used
* **Jadex** -- Multi-agent system for the development of distributed applications. This approach combines the hierarchical structure of components ([SCA](https://en.wikipedia.org/wiki/Service_Component_Architecture "Service Component Architecture")) with the ability to abstractly implement business logic based on [BDI-агентов](https://en.wikipedia.org/wiki/Belief%E2%80%93desire%E2%80%93intention_software_model "Belief–desire–intention software model").

  The system is based on services, that is, for each object a certain dependence is assigned to the environment, which is described with the help of services that the facility should receive from others and those services that it provides. The interaction of objects is completely controlled by the system, so it becomes possible to store code for objects written on different machines.
* Java and xml -- to work with the source code of the system, as well as to write your agent.
* Gradle -- to build the project.

### Content
As the main example, on which the Jadex system will be studied, the following model is taken.

There is a forest, bears live in the forest. She is a bear family consisting of a Bear, a Bearwife and a lair in which they live. The goal of each family is to make as many jams as possible. For the jam you need two ingredients: raspberries and water (2:1).

Behind the raspberry is a bear. Every day he wanders through the forest in search of crimson bushes. Finding them, he collects raspberries in his basket, and as soon as it is filled, he puts the raspberries in his lair. During the day, the bear remembers all the crimson bushes met on the way, so in case of what can return to them.

The bearwife walks after the water - she finds a river in the forest, and every time when the water ends in the lair, she goes to her stockpile. If she meets raspberry bushes along the way, she informs about this to her bear.

Jam is also made by a bearwife: if she is in a lair and there are reserves of water and raspberries in it.

At night, everyone returns to their lairs, even if the raspberries / water has not yet been recruited. Overnight, the bear forgets the location of all the raspberry bushes seen except the last one. Bearwife does not forget the location of the river.

Representatives of different bear families may meet each other.
* If two bears meet, then they fight. Randomly determined victor, who takes away from the defeated so much raspberries, how much can fit in his basket.
* If there are two bearwifes, then they start chatting with each other about this and that, wasting time.
* If the bear meets another's bearwife, it hangs for a while and forgets the location of all the raspberry bushes seen.

### Architecture
The application can be conditionally divided into two parts: the environment and agents. Here agents are bears, and the environment is forest, bushes, river, bears, and all possible interactions between objects.

#### Environment
Each object (bear, bush, river, etc.) is represented by its class with the basic functions of changing the fields (for example, to update the amount of raspberries in the basket). For convenience, they are all inherited from the abstract class `LocationObject`. The class `Vision` is also defined, which describes that part of the location that the agent sees at the moment. The `Environment` class describes actions on the environment and various interactions (for example, picking raspberries from a bush).

#### Agents
Each agent has his own ideas about the world (for example, what he sees / does not see), goals and plans. Later, they are connected through avatars to the environment and begin to interact with it.

### Building
First you need to download Gradle. After that, you can build the project with one command: `gradle build`. All the necessary files (.class and .jar) will be in the build folder.

### Work plan
* [x] install Jadex, learn how to run the examples;
* [x] deal with the documentation;
* [x] understand the basic details of the Jadex architecture;
* [x] create your own environment based on existing examples.
* [x] Understand the work of 3 examples of coalition tasks and in the code of agents that solve them;
* [ ] create your own environment to work on these tasks.
* [ ] the implementation of its simple agent in the form of a plug-in to Jadex.

## Conclusion
I was not interested in bringing the project to its logical conclusion. Only the environment (objects and interactions between them) is realized, but without visualization, agents and plans.

Thanks to this project, I now know that I'm not interested in doing such tasks. As it seems to me, this is a rather useful result.
