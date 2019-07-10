# ArrayScript
Some kind of programming language I am creating for fun. Everything is work in progress and far away from use in applications.

I thought it would be a funny idea to create my own programming language. I will see how far I will get and I'm not expecting that I will finish this language.

It will be a statically typed language that will be compiled to plain javascript or html with large script tags. All tools to manage the language will be written in Java. So I am planning to write a java program that will parse the ArrayScript sources and transpile them to JavaScript or HTML.

The current name 'ArrayScript' is based on the idea of lettings all ArrayScript objects become arrays in JavaScript as a dirty performance trick. I will likely change this name in the future if I continue with this (somewhat crazy) project. Also, the first transpiler will likely not do any performance optimizations, I would already be glad if I could make it work properly.

This repository contains (at least at the time of writing this) the java projects ObjectModel and Parser. It also contains the folder 'language'. The Parser contains the source code for the parser program that is supposed to read ArrayScript source files and puts them in java objects whose classes are defined in ObjectModel. (I yet have to create Transpiler projects that use those objects to generate javascript code.) The language folder is a folder containing text files (and folders containing text files) that should describe the language.