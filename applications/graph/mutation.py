
import langchain
import os
from langchain.docstore.document import Document

langchain.debug=True


from langchain_experimental.graph_transformers.diffbot import DiffbotGraphTransformer

diffbot_api_key = os.environ.get("DIFFBOT_API_KEY")
diffbot_nlp = DiffbotGraphTransformer(diffbot_api_key=diffbot_api_key)

from langchain.document_loaders import WikipediaLoader

# raw_document1 = Document(page_content="""
# 장진영씨는 2023년 1월에 유엔진에 입사했다.
# 장진영씨가 2023년 2월에 대표이사로 승진했다.
# 장진영씨의 휴가일수는 10일이다.
# 장진영씨는 경영지원팀의 팀장이자 대표이사다.
# 경영지원팀에는 서원주씨가 사원으로 근무한다.
# 서원주씨의 남은 휴가일수는 10일이다.
# 장진영씨가 휴가를 하루 썼다.
# 서원주는 휴가를 이틀 사용했다.
# """, metadata={"source": "local"})

raw_document1 = Document(page_content="""
Bob is a member of uenginesolutions
Bob's remaining vacation days is 15 days.
""", metadata={"source": "local"})

raw_documents = []


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

