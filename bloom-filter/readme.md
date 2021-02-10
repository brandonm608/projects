#Various Utility Data Structures

These projects are incomplete and likely could use some tidying up. The goal of these projects is purely exploration. The algorithms implemented here are nothing new.

##Bloom Filter

A simple bloom filter

##Murmur3
A copy and modification of the Murmur3 C/C++ implementation given by Peter Scott's github page. Peter Scott created a port of the Murmur3 algorithm and mentions Austin Appleby as the original creator.

I use Murmur3 in the Cuckoo Hash and Cuckoo Filter

##Cuckoo Hash and Filter

Exploration of Cuckoo hash and filter. The Cuckoo Hash is implemented as both a Set and Map. Collisions are resolved through rehashing. However this may not protect against manufactured collisions.

##Linked List Hash Set and Hash Map

A simple linked lest hash set which uses the built-in hash() method Java provides. Linked Lists are used to resolve collisions.

##Priority Queue

An incomplete implementation of a heap using a binary tree.

##2-3 Tree

Exploration of 2-3 trees. Abandoned in favor of a straight red-black tree

##2-3-4 Tree

Exploration of 2-3-4 trees. Also abandoned in favor of a straight red-black tree

##RBTreeSet

Exploration of straight red-black trees. Performance is close to that of the built-in openJDK11 algorithm. The times may vary depending on time spent on tasks outside of the main application code. There is still work to be done to remove items from the tree. Future exploration may be to utilize this implementation in conjunction with a Cuckoo hash using Java's built-in hash() method for Objects. The Red-Black Tree would provide better performance than a linked-list in resolving high collisions in a Cuckoo hash