# 50.054 Code Generation

## Learning Outcomes

1. Apply SSA-based register allocation to generate 3-address code from Pseudo Assembly
1. Handle register spilling



## Exercise 1 


Consider the following PA SSA code.

```js
1: x1 <- input1
2: f1 <- 0
3: s1 <- 1
4: c1 <- 0
5: c2 <- phi(4:c1, 11:c3)
   f2 <- phi(4:f1, 11:f3)
   s2 <- phi(4:s1, 11:s3)
   b1 <- c2 < x1
6: ifn b1 goto 12
7: t3 <- f2
8: f3 <- s2
9: s3 <- t3 + f3
10: c3 <- c2 + 1
11: goto 5
12: r_ret <- s2
13: ret
```

Apply the extended liveness analysis to above PA SSA program.


## Exercise 2 

Continue from Exercise 1. Generate the live range table. 



## Exercise 3

Continue from Exercise 2. Generate the 3 address target code with 4 registers.



## Optional Challenge Exercise 4 

Can you generate the 3 address target code with 4 registers with the following registers coalesced?

* `c1` and `c3` share the same register.
* `f1` and `f3` share the same register.
* `s1` and `s3` share the same register.