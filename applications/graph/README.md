# langchain-graph

```
pipenv install
pipenv shell

```

and set the python interpreter by pressing "Cmd+P" and enter ">Python"

the interpreter name should be like "langchain-graph" and "PipEnv" something.

before you run, need to launch neo4j firstly:

```
docker run \
    --name neo4j \
    -p 7474:7474 -p 7687:7687 \
    -e NEO4J_AUTH=neo4j/pleaseletmein \
    -e NEO4J_PLUGINS=\[\"apoc\"\]  \
    neo4j:latest
```


For debug mode:

```
handler = StdOutCallbackHandler()
chain.run(... , callbacks=[handler])
```