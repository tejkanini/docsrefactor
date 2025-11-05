```python
## Change 1 ##
# Replace openai package with ai_server
import ai_server
import streamlit as st

## Change 2 ##
# Create a function to login into the server using your access keys
@st.cache_resource # Use st.cache to cache the server authentication logic
def login(secret_key, access_key, ai_server_url):
    try:
        # Attempt to create the server connection. If there is an error when connecting then an error will be raised
        conn = ai_server.RESTServer(
            access_key=access_key,
            secret_key=secret_key,
            base=ai_server_url
        )
        return conn
    except:
        # If login fails, display an error message
        st.error("Login failed")

## Change 3 ##
st.title("💬 Chatbot")
# Change the display reference from OpenAI LLM to SEMOSS Server
st.caption("🚀 A streamlit chatbot powered by SEMOSS Server")

# Remove OpenAI secret and add additional inputs in the side bar menu
with st.sidebar:
    ai_server_url = st.text_input("AI Server URL")                          # Your Monolith endpoint
    secret_key = st.text_input("Secret Key", type="password")               # Your AI Server Secret Key
    access_key = st.text_input("Access Key", type="password")               # Your AI Server Access Key
    engine_id = st.text_input("Model Engine ID", key="model_engine_id")     # The model engine ID
    server_connection = login(secret_key, access_key, ai_server_url)        # make the login call


if "messages" not in st.session_state:
    st.session_state["messages"] = [{"role": "assistant", "content": "How can I help you?"}]

for msg in st.session_state.messages:
    st.chat_message(msg["role"]).write(msg["content"])

## Change 4 ##
# When the prompt is provided, make the connection was established and an engine ID was provided
if prompt := st.chat_input():
    if not server_connection:
        st.warning("Please login before trying to make an inference call.")
        st.stop()

    if not engine_id:
        st.info("Please add Model Engine ID.")
        st.stop()

    ## Change 5 ##
    ## Create a reference to the model engine ##
    model = ai_server.ModelEngine(engine_id = engine_id, insight_id = server_connection.cur_insight)

    # No changes here
    st.session_state.messages.append({"role": "user", "content": prompt})
    st.chat_message("user").write(prompt)

    # update the inference call to use ModelEngine
    response = model.ask(question = prompt)
    # Get the output string from the response
    msg = response[0]['response']
    # match the dictionary responses for OpenAI
    st.session_state.messages.append({"role": "assistant", "content": msg})
    st.chat_message("assistant").write(msg)
```
