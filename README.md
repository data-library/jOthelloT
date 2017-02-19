jOthelloT (Java Othello Tournament)
=====
#### Abstract 

<div style="width:1000px; text-align:justify;text-justify: inter-word;">
Introductory artificial intelligence undergraduate classes often introduce different search methods using different search algorithms. In this context one of algorithms that is often taught, is the minimax algorithm which is used in adversarial games where you want to minimize your opponent’s chance of winning while maximizing your chance of winning. Different instructors use different games to make the students implement the minimax algorithm such as Checkers, Othello or Chess. However, one common problem with this assignment is that the students often spend more time implementing the game itself rather than the artificial intelligence techniques in the game. For this reason, in this paper we present a java-based open source Othello framework that was designed to be used in artificial intelligence undergraduate classes. Our framework has several features that help the students to focus on the development of the artificial intelligence aspects of the game, rather than developing the game itself. One particular feature of the framework is that it has a method that returns the list of valid moves given the current state of the game board and which player is going to make the next move. With this method, the students can focus on how to evaluate the different states using several heuristic functions and implementing the minimax algorithm. Another feature of the framework is the graphical user interface and the HumanPlayer class that allows the students to play against their own code. This feature is important as it allows the students to not only debug their codes but also to evaluate the effectiveness of their implemented heuristics. Another aspect of the framework is that it allows to set up a tournament of the codes developed by the students. The tournament can be organized in two modes. In the first mode every AI developed by one student plays against the AI developed by every other student. In the second mode, each student developed code is paired against another student developed code and only the winner plays against the winner of another pairing until there is only one winner left. An analysis of the framework in our artificial intelligence undergraduate computer engineering classes shows that it properly supports the student learning and the tournament mode also challenges them to create the best AI for Othello as they can.
</div>
<div style="width:1000px; text-align:justify;text-justify: inter-word;">
More information about jOthelloT and it's use in the classroom can be found <a href="http://ieeexplore.ieee.org/document/7757577/" target="_blank"> here</a>.
</div>
Contributors
============
Marcelo Paglione, Iuri Mardegan, Carlos N. Silla Jr.

License
=======
MIT-License, see LICENSE file.