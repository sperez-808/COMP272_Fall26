# COMP 272 — After-Class Activity: Cuckoo Hashing

In light of this week's second lecture about Cuckoo Hashing, answer the following questions in your own words. 

1. **Finding and deleting a key.** In cuckoo hashing with two hash functions, where can a key be stored? Explain why lookup requires at most two probes and why deletion does not require a tombstone, unlike linear probing.

2. **Following the kick-outs.** Start with two empty tables, T1 and T2. Insert X, Y, and Z in that order, always trying T1 first:

   | Key | h1(key) | h2(key) |
   |---|---:|---:|
   | X | 2 | 1 |
   | Y | 0 | 2 |
   | Z | 2 | 0 |

   Show the final location of each key and explain each displacement.

3. **When insertion gets stuck.** A student says: “If the table still has empty slots, cuckoo hashing must be able to insert a new key.” Is this correct? Explain how an insertion can enter a cycle and how the implementation should recover.

4. **Open question — Efficiency versus fairness.** In class, we discussed the filtering out of rare IDs and removing inactive IDs to save memory in a recommendation system. How might these choices affect new users or creators with less popular content? Propose one adjustment that balances memory efficiency with giving them a chance. Support your answer with a concrete example or a brief outside source; cite any source you use.
