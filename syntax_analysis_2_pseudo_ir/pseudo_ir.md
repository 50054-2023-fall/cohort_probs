# 50.054 Pseudo Intermediate Representation

## Learning Outcomes

* Apply Maximal Munch Algorithm to generate Psuedo IR from SIMP
* Apply Maximal Munch Algorithm V2 to generate optimized Psuedo IR from SIMP


## SIMP language

Recall 

$$
\begin{array}{rccl}
(\tt Statement) & S & ::= & X = E ; \mid return\ X ; \mid nop; \mid if\ E\ \{ \overline{S} \}\ else\ \{ \overline{S} \} \mid while\ E\ \{ \overline{S} \} \\
(\tt Expression) & E & ::= & E\ OP\ E \mid X \mid C  \mid (E) \\
(\tt Statements) & \overline{S} & ::= & S \mid S\ \overline{S} \\
(\tt Operator) & OP & ::= & + \mid - \mid * \mid <  \mid == \\ 
(\tt Constant) & C & ::= & 0 \mid 1 \mid 2 \mid ... \mid true \mid false \\ 
(\tt Variable) & X & ::= & a \mid b \mid c \mid d \mid ...
\end{array}
$$

## Pseudo Assembly


$$
\begin{array}{rccl}
(\tt Labeled\ Instruction) & li  & ::= & l : i \\ 
(\tt Instruction)   & i   & ::= & d \leftarrow s \mid d \leftarrow s\ op\ s \mid ret \mid ifn\ s\ goto\ l \mid goto\ l \\ 
(\tt Labeled\ Instructions)   & lis   & ::= & li \mid li\ lis \\ 
(\tt Operand)       & d,s & ::= & r \mid c \mid t \\
(\tt Temp\ Var)      & t   & ::= & x \mid y \mid ...  \\
(\tt Label)         & l   & ::= & 1 \mid 2 \mid ... \\
(\tt Operator)      & op  & ::= & + \mid - \mid * \mid < \mid == \\ 
(\tt Constant)      & c   & ::= & 0 \mid 1 \mid 2 \mid ... \\ 
(\tt Register)      & r &   ::= & r_{ret} \mid r_1 \mid r_2 \mid ...  
\end{array}
$$

In Pseudo Assembly, we use `0` to denote `false` and any `1` constant to denote `true`.

## Maximal Munch Algorithm

$$
\begin{array}{rc}
{\tt (mAssign)} & \begin{array}{c} 
               G_a(X)(E) \vdash lis  \\
               \hline
               G_s(X = E) \vdash lis
               \end{array} \\ 
\end{array}  
$$

$$
\begin{array}{rc}
{\tt (mReturn)} & \begin{array}{c}
     G_a(r_{ret})(X) \vdash lis \ \ l\ {\tt is\ a\ fresh\ label} \\
     \hline
     G_s(return\ X) \vdash lis + [ l: ret ]
     \end{array}
\end{array}
$$


$$
\begin{array}{rc}
{\tt (mSequence)} & \begin{array}{c} 
               {\tt for}\ l \in \{1,n\} ~~ G_s(S_l) \vdash lis_l \\
               \hline
               G_s(S_1;...;S_n) \vdash lis_1 + ... +  lis_n
               \end{array} 
\end{array}  
$$


$$
\begin{array}{rl}
     {\tt (mIf)} & \begin{array}{c}
               t\ {\tt is\ a\ fresh\ var} \\ 
               G_a(t)(E) \vdash lis_0 \\
               l_{IfCondJ}\ {\tt is\ a\ fresh\ label} \\
               G_s(S_2) \vdash lis_2 \\ 
               l_{EndThen}\ {\tt  is\ a\ fresh\ label} \\  
               l_{Else}\ {\tt is\ the\ next\ label (w/o\ incr)} \\ 
               G_s(S_3) \vdash lis_3 \\ 
               l_{EndElse}\ {\tt is\ a\ fresh\ label} \\
               l_{EndIf}\ {\tt is\ the\ next\ label\ (w/o\ incr)} \\ 
               lis_1 = [l_{IfCondJ}: ifn\ t\ goto\ l_{Else} ] \\ 
               lis_2' = lis_2 + [l_{EndThen}:goto\ l_{EndIf}] \\ 
               lis_3' = lis_3 + [l_{EndElse}:goto\ l_{EndIf}] \\ 
               \hline  
               G_s(if\ E\ \{S_1\}\ else\ \{S_2\}) \vdash lis_0 + lis_1 + lis_2' + lis_3'               
                \end{array} \\  
\end{array}
$$

$$
\begin{array}{rl}
     {\tt (mWhile)} & \begin{array}{c}
                    l_{While}\ {\tt is\ the\ next\ label\ (w/o\ incr)} \\ 
                    t\ {\tt is\ a\ fresh\ var} \\     
                    G_a(t)(E) \vdash lis_0 \\ 
                    l_{WhileCondJ}\ {\tt is\ a\ fresh\ label} \\ 
                    G_s(S) \vdash lis_2\\ 
                    l_{EndBody}\ {\tt is\ a\ fresh\ label} \\  
                    l_{EndWhile}\ {\tt is\ the\ next\ label\ (w/o\ incr)} \\ 
                    lis_1 = [l_{WhileCondJ}: ifn\ t\ goto\ l_{EndWhile}] \\
                    lis_2' = lis_2 + [ l_{EndBody}: goto\ l_{While} ] \\
                    \hline
                    G_s(while\ E\ \{S\}) \vdash lis_0 + lis_1 + lis_2'           
                \end{array} \\  
\end{array}
$$

$$
\begin{array}{rc}
{\tt (mConst)} & \begin{array}{c} 
              l\ {\tt  is\ a\ fresh\ label}\\ c = conv(C) \\
               \hline
               G_a(X)(C) \vdash [l : X \leftarrow c] 
               \end{array} \\ 
\end{array}  
$$

In the above rule, given a SIMP variable $X$ and a constant $C$ we generate a labeled instruction $X \leftarrow c$. where $c$ is the PA constant converted from SIMP's counter-part through the $conv()$ function. 

$$
\begin{array}{rcl}
conv(true) & = &  1\\
conv(false) & = & 0\\
conv(C) & =&  C
\end{array}
$$


$$
\begin{array}{rc}
{\tt (mVar)} & \begin{array}{c} 
              l\ {\tt  is\ a\ fresh\ label} \\
               \hline
               G_a(X)(Y) \vdash [l : X \leftarrow Y] 
               \end{array} \\ 
\end{array}  
$$

$$
\begin{array}{rc}
{\tt (mOp)} & \begin{array}{c} 
              t_1\ {\tt is\ a\ fresh\ var} \\ 
              G_a(t_1)(E_1) \vdash lis_1 \\ 
              t_2\ {\tt is\ a\ fresh\ var} \\ 
              G_a(t_2)(E_2) \vdash lis_2 \\ 
              l\ {\tt  is\ a\ fresh\ label} \\
               \hline
               G_a(X)(E_1 OP E_2) \vdash lis_1 + lis_2 + [l : X \leftarrow t_1 OP t_2] 
               \end{array} \\ 
\end{array}  
$$



Applying Maximal Munch Algorithm to the following SIMP program

```python
x = input;
s = 0;
c = 0;
while c < x {
    s = c + s;
    c = c + 1;
}
return s;
```

1. Firstly we apply ${\tt (mSequence)}$ rule to individual statement,

```
Gs(x = input; s = 0; c = 0;
while c < x {
    s = c + s;
    c = c + 1;
}
return s;) 
--->
Gs(x = input) ; Gs( s = 0) ; Gs(c = 0) ; Gs( while c < x  { s = c + s; c = c + 1;}) ; Gs(return s);
```

The derivation for `Gs(x = input)` is trivial, we apply ${\tt (mAssign)}$ rule. 
```
Gs(x = input) 
---> # using (mAssign) rule
Ga(x)(input)
---> # using (mVar) rule
---> [ 1: x <- input ] 
```
Similarly we generate 

```
Gs( s = 0)
---> # using (mAssign) rule
Ga(s)(0)
---> # using (mConst) rule
---> [ 2: s <- 0 ] 
``` 

and 

```
Gs(c = 0)
---> # using (mAssign) rule
Ga(c)(0)
---> # using (mConst) rule
---> [ 3: c <- 0 ] 
``` 

2. Next we consider the while statement

```
Gs(
while c < x {
    s = c + s;
    c = c + 1;
}
)
---> # using (mWhile) rule
  # the condition exp
  t is a fresh var
  Ga(t)(c<x) ---> # using (mOp) rule
     t1 is a fresh var
     Ga(t1)(x) ---> [4: t1 <- x]
     t2 is a fresh var 
     Ga(t2)(c) ---> [5: t2 <- c]
  ---> [4: t1 <- x, 5: t2 <-c, 6: t <- t1 < t2 ]
  # the conditional jump, we generate a new label 7 reserved for whilecondjump
  # the while loop body
  Gs[ s = c + s; c = c + 1]
  ---> # (mSequence), (mOp) and (mOp) rules
  [ 8: t3 <- c, 9: t4 <- s, 10: t5 <- t3 + t4,  11: t6 <- c, 12: t7 <- 1, 13: t8 <- t6 + t7 ]
  # end of the while loop
  [ 14: goto 4 ]
  # the conditional jump 
  ---> [7: ifn t goto 15 ]
--->  # putting altogther
[4: t1 <- x, 5: t2 <- c, 6:  t <- t1 < t2,   7: ifn t goto 15, 
 8: t3 <- c, 9: t4 <- s, 10: t5 <- t3 + t4,  11: t6 <- c, 
 12: t7 <- 1, 13: t8 <- t6 + t7, 14: goto 4] 
```

3. Finally we convert the return statement
```
Gs(return s)
---> # (mReturn) rule
[15: r_ret <- s, 16: ret]
```

Putting 1,2,3 together

```
1: x <- input
2: s <- 0
3: c <- 0
4: t1 <- x
5: t2 <- c
6: t <- t1 < t2
7: ifn t goto 15 
8: t3 <- c
9: t4 <- s
10: t5 <- t3 + t4
11: t6 <- c 
12: t7 <- 1 
13: t8 <- t6 + t7
14: goto 4
15: rret <- s
16: ret
```

The algorithm is implemented in the project template (TODO).

As we observed during the class, the naive Maximal munch algorithm does not produce an optimal PA code. 

We proposed the following alternative. 


$$
\begin{array}{rc}
{\tt (m2Assign)} & \begin{array}{c} 
     G_e(E) \vdash (\^{e}, \v{e})  \ \ 
     l\ {\tt is\ a\ fresh\ label.} \\ 
     \hline
     G_s(X = E) \vdash \v{e} + [ l : X \leftarrow \^{e}]
     \end{array} \\ 
\end{array}  
$$


$$
\begin{array}{rc}
{\tt (m2Return)} & \begin{array}{c}
     G_e(X) \vdash (\^{e}, \v{e}) \ \ l_1, l_2\ {\tt are\ fresh\ labels} \\
     \hline
     G_s(return\ X) \vdash \v{e} + [ l_1 : r_{ret} \leftarrow \^{e},  l_2: ret ]
     \end{array} 
\end{array}
$$


$$
\begin{array}{rl}
{\tt (m2If)} & \begin{array}{c}
          G_e(E) \vdash (\^{e}, \v{e}) \\ 
          l_{IfCondJ}\ {\tt is\ a\ fresh\ label} \\
          G_s(S_2) \vdash lis_2 \\ 
          l_{EndThen}\ {\tt  is\ a\ fresh\ label} \\  
          l_{Else}\ {\tt is\ the\ next\ label (w/o\ incr)} \\ 
          G_s(S_3) \vdash lis_3 \\ 
          l_{EndElse}\ {\tt is\ a\ fresh\ label} \\
          l_{EndIf}\ {\tt is\ the\ next\ label\ (w/o\ incr)} \\ 
          lis_1 = [l_{IfCondJ}: ifn\ \^{e}\ goto\ l_{Else} ] \\ 
          lis_2' = lis_2 + [l_{EndThen}:goto\ l_{EndIf}] \\ 
          lis_3' = lis_3 + [l_{EndElse}:goto\ l_{EndIf}] \\ 
          \hline  
          G_s(if\ E\ \{S_1\}\ else\ \{S_2\}) \vdash \v{e} + lis_1 + lis_2' + lis_3'               
          \end{array} 
\end{array}
$$


$$
\begin{array}{rl}
{\tt (m2While)} & \begin{array}{c}
          l_{While}\ {\tt is\ the\ next\ label\ (w/o\ incr)} \\ 
          G_e(E) \vdash (\^{e}, \v{e}) \\ 
          l_{WhileCondJ}\ {\tt is\ a\ fresh\ label} \\ 
          G_s(S) \vdash lis_2\\ 
          l_{EndBody}\ {\tt is\ a\ fresh\ label} \\  
          l_{EndWhile}\ {\tt is\ the\ next\ label\ (w/o\ incr)} \\ 
          lis_1 = [l_{WhileCondJ}: ifn\ \^{e}\ goto\ l_{EndWhile}] \\
          lis_2' = lis_2 + [ l_{EndBody}: goto\ l_{While} ] \\
          \hline
          G_s(while\ E\ \{S\}) \vdash  \v{e} + lis_1 + lis_2'           
          \end{array} 
\end{array}
$$


$$ 
\begin{array}{rc}
{\tt (m2Const)} & \begin{array}{c} 
          G_e(C) \vdash (conv(C), []) 
          \end{array} 
\end{array}  
$$


$$
\begin{array}{rc}
{\tt (m2Var)} & \begin{array}{c} 
          G_e(Y) \vdash (Y, []) 
     \end{array} 
\end{array}  
$$

$$
\begin{array}{rc}
{\tt (m2Op)} & \begin{array}{c} 
          G_e(E_1) \vdash (\^{e}_1, \v{e}_1) \\ 
          G_e(E_2) \vdash (\^{e}_2, \v{e}_2) \\ 
          t \ {\tt is\ a\ fresh\ variable.} \\ 
          l \ {\tt is\ a\ fresh\ label.} \\ 
          \hline
          G_e(E_1 OP E_2) \vdash (t, \v{e}_1 + \v{e}_2 + [l : t \leftarrow \^{e}_1 OP \^{e}_2]) 
          \end{array} \\ 
\end{array}  
$$



1. Firstly we apply ${\tt (m2Sequence)}$ rule to individual statement,

```
Gs(x = input; s = 0; c = 0;
while c < x {
    s = c + s;
    c = c + 1;
}
return s;) 
--->
Gs(x = input) ; Gs( s = 0) ; Gs(c = 0) ; Gs( while c < x  { s = c + s; c = c + 1;}) ; Gs(return s);
```

The derivation for `Gs(x = input)` is trivial, we apply ${\tt (m2Assign)}$ rule. 
```
Gs(x = input) 
---> # using (m2Assign) rule
    Ge(input) 
    ---> # using (m2Var) rule
      (input, []) 
--> [1: x <- input]
```
Similarly we generate 

```
Gs(s = 0)
---> # using (m2Assign) rule
    Ge(0)
    ---> # using (m2Const) rule
      (0, [])
---> [2: s <- 0] 
``` 

and 

```
Gs(c = 0)
---> # using (m2Assign) rule
    Ge(0)
    ---> # using (m2Const) rule
      (0, []) 
---> [ 3: c <- 0 ] 
``` 

2. Next we consider the while statement

```
Gs(
while c < x {
    s = c + s;
    c = c + 1;
}
)
---> # using (m2While) rule
  # the condition exp
  Ge(c<x) ---> # using (m2Op) rule
     t is a fresh var
     Ge(c) ---> (c, [])
     Ge(x) ---> (x, [])
  [4: t <- c < x ]
  
  # the conditional jump, we generate a new label 5 reserved for whilecondjump
  # the while loop body
  Gs[ s = c + s; c = c + 1]
  ---> # (m2Sequence), (m2Op) and (m2Op) rules
  [ 6: s <- c + s; 7, c <- c + 1; ]
  # end of the while loop
  [ 8: goto 4 ]
  # the conditional jump 
  ---> [5: ifn t goto 9 ]
--->  # putting altogther
[4: t1 <- c < x, 5: ifn t goto 9, 
  6: s <- c + s, 7: c <- c + 1, 8: goto 4] 
```

3. Finally we convert the return statement
```
Gs(return s)
---> # (m2Return) rule
[9: rret <- s, 10: ret]
```

Putting 1,2,3 together

```
1: x <- input
2: s <- 0
3: c <- 0
4: t <- c < x 
5: ifn t goto 9
6: s <- c + s
7: c <- c + 1
8: goto 4
9: rret <- s
10: ret
```

Your first two tasks in the project is to use Scala to
1. develop a top-down parser to parse a SIMP program
1. implement the V2 of Maximal Munch algorithm to generate a PA from the parsed SIMP program.