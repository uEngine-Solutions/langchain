
import langchain
import os
from langchain.docstore.document import Document
from langchain_community.document_loaders import DirectoryLoader


langchain.debug=True


from langchain_experimental.graph_transformers.diffbot import DiffbotGraphTransformer

diffbot_api_key = os.environ.get("DIFFBOT_API_KEY")
diffbot_nlp = DiffbotGraphTransformer(diffbot_api_key=diffbot_api_key)


#raw_documents = DirectoryLoader('/workspace/langchain/applications/graph/src', glob="**/*.java").load()


raw_documents = []



with open('applications/graph/merged_text_files.txt', 'r') as file:
    content = file.read()
    raw_document1 = Document(page_content=content, metadata={"source": "local"})
    raw_documents.append(raw_document1)

graph_documents = diffbot_nlp.convert_to_graph_documents(raw_documents)


from langchain.graphs import Neo4jGraph

url = "bolt://localhost:7687"
username = "neo4j"
password = "pleaseletmein"

graph = Neo4jGraph(url=url, username=username, password=password)



graph.add_graph_documents(graph_documents)
graph.refresh_schema()

from langchain.chains import GraphCypherQAChain
from langchain.chat_models import ChatOpenAI

from langchain.callbacks.base import BaseCallbackHandler
from typing import Any, Dict, List, Union


class MyCustomHandler(BaseCallbackHandler):
    def on_text(self, text: str, **kwargs: Any) -> Any:
        print(f"Text: {text}")
        self.log = text
  
    def on_chain_start(
        self, serialized: Dict[str, Any], inputs: Dict[str, Any], **kwargs: Any
    ) -> Any:
        """Run when chain starts running."""
        print("Chain started running")


handler = MyCustomHandler()

chain = GraphCypherQAChain.from_llm(
    cypher_llm=ChatOpenAI(temperature=0, model_name="gpt-4"),
    qa_llm=ChatOpenAI(temperature=0, model_name="gpt-3.5-turbo"),
    graph=graph,
    verbose=True,
    callbacks=[handler]
)

chain.run("장진영씨의 남은 휴가일수는?")

