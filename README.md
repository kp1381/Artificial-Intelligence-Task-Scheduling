# Artificial-Intelligence-Task-Scheduling
<TITLE>  Assignment Description </TITLE>

<H4> Assignment </H4>
Write a program that solves the scheduling problem described in 
<A href="hwk1.pdf"> Problem Set 1,</A>  
using the following algorithm:
<UL>
<LI> In the first stage of search,
use a breadth-first search through the state space of problem set 1,
until the queue of frontier states has reached a specified
maximum. Implement the repeated state elimination discussed in problem 3.  
<LI> In the second stage of search, use iterative deepening starting
from the frontier created in the first stage.
</UL>

<H4> Input </H4>
The input to your code should be a plain text file of the following format:
<BLOCKQUOTE>
<b>First Line:</b> The number of tasks, the target value, the deadline, and the
maximum size of the queue <br>
<b>Next N lines:</b> Each line is a triple TaskId, Value, Time Requirement
separated by white space. The TaskId is a number between 0 and N-1. <br>
<b>Remainder</b> The remainder of the file is an encoding of the prerequisite
DAG. It is a sequence of lines of the form X Y meaning that X is a 
prerequisite for Y.
</BLOCKQUOTE>
<p>
For example, the example in Problem set 1 would be input as follows:
<PRE>
5 12 7 3
0 3 3
1 6 2
2 4 3
3 1 6
4 4 1
0 1
2 3
4 1
4 3
</PRE>

You may assume that the input is correctly formatted and that the arcs
describe a DAG. You do not have to do error handling for incorrect inputs.

<H4> Output </H4>
The output should either have the form<br>
List of tasks, total value, total time <br>
or 0 if there is no solution.

For example the output for the above example would be
<PRE>
[0 4 1] 13 6
</PRE>

<H4> Experimentation </H4>
You should create a driver for the program that takes a single argument,
N, the number of tasks, and E, the number of experiments; creates E random
examples each of size N; runs the search code; and then does some statistics
on the results.

<p>
You can create a random example as follows:
<UL>
<LI> Create N tasks.
<LI> Assign values and time randomly uniformly between 0 and N</sup>.
<LI> Choose the target value and the deadline randomly between 
N<sup>2</sup>(1 - 2/sqrt(N))/4 and
N<sup>2</sup>(1 + 2/sqrt(N))/4 
<LI> Create a random DAG as follows:
<PRE>
   P = [1,2, ... N]
   for (I=1 to N) {       /* Construct a random permutation */
      J = a random value between I and N;
      swap(P[I],P[J])
     }
    for (I=1 to N-1)
       for (J = I+1 to N)
          with probability 1/2 create an arc from P[I] to P[J];
</PRE>
</UL>

You may either write the example to a text file and use that as input
to your search program, or directly call the top-level function
of the search program.

<p>
Test your program with increasing values of N and with E = 1000, up to the
point where the running time becomes impossible. For each value of N,
record

<UL>
<LI> The fraction of examples where a solution exists.
<LI> The maximum, minimum, and average number of states created in the
search process.
<LI> Any other statistics that interest you.
</UL>
