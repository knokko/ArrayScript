# ArrayScript
Some kind of programming language I am creating for fun.

I thought it would be a funny idea to create my own programming language. I will see how far I will get and I'm not expecting that I
will finish this language.

It will be a statically typed language that will be compiled to plain javascript or html with large script tags. All tools to manage
the language will be written in Java. So I am planning to write a java program that will parse the ArrayScript sources and compile them
to JavaScript or HTML.

In the JavaScript codes, a large global array will be created for most primitive types in javascript.
I will try to to some really ugly performance tricks in the generated JavaScript code. For now, all ArrayScript classes will be number 
arrays in JavaScript that contain the indices for the right large global array where the property of the object will be stored.

The language will have mostly manual memory management. When objects are discarded, the indices of the arrays will be made available
for other variables or object properties.
