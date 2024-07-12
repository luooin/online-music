<template>
  <div class="chat-window">
    <div class="online-users">
      <h3 class="online-users-title">在线用户</h3>
      <ul class="online-users-list">
        <li v-for="user in onlineUsers" :key="user">{{   user  }}</li>
      </ul>
    </div>
    <div class="chat-box">
      <div class="chat-history">
        <div v-for="msg in messages" :key="msg.id">
          <!-- 系统消息 -->
          <div v-if="msg.from === '系统: '" class="system-message">
            {{ msg.message }}
          </div>
          <!-- 非系统消息 -->
          <div v-else :class="[msg.type === 'sent' ? 'align-right' : 'align-left', 'message']">
            <div class="info">
              <span class="from">{{ msg.from }}</span>
              <span class="date">{{ msg.date }}</span>
            </div>
            <div class="bubble">{{ msg.message }}</div>
          </div>
        </div>
      </div>
      <div class="chat-input">
        <input class="inPutting" type="text" v-model="inputMessage" @keyup.enter="sendMessage" placeholder="请输入消息..."/>
        <button @click="sendMessage">发送</button>
      </div>
    </div>
  </div>
</template>
<script>
import {getCurrentInstance, nextTick, onBeforeUnmount, onMounted, reactive, toRefs} from "vue";

export default {
  name: "ChatRoom",
  setup() {
    const state = reactive({
      inputMessage: '',
      messages: [],
      username: '',
      ws: null,
      onlineUsers: [], // 新增：用于存储在线用户列表
    });

    // Vue 3 Composition API 生命周期钩子
    onMounted(() => {
      setUsername();
    });

    onBeforeUnmount(() => {
      if (state.ws) {
        state.ws.close();
      }
    });

    function setUsername() {
      const {proxy} = getCurrentInstance();
      state.username = proxy.$store.getters.username+" "; // 加空格与时间隔开，美观
      state.ws = new WebSocket(`ws://localhost:8888/websocket/${state.username}`);
      state.ws.addEventListener('message', (event) => {
        const data = JSON.parse(event.data);
        if (data.type === 'onlineStatus') {
          // 处理在线状态消息
          state.onlineUsers = data.onlineUsers;
        } else {
          // 处理普通聊天消息
          const currentTime = new Date().toLocaleString();
          const receivedMessage = {...data, type: 'received', id: state.messages.length, date: currentTime};
          state.messages.push(receivedMessage);
          scrollToBottom();
        }
      });
    }

    function sendMessage() {
      if (state.inputMessage.trim() === '') return;

      const currentTime = new Date().toLocaleString();
      const message = {
        from: state.username,
        date: currentTime,
        message: state.inputMessage.trim(),
        type: 'chat', // 指定消息类型为聊天消息
      };
      // 尝试发送消息
      try {
        state.ws.send(JSON.stringify(message));
        state.messages.push({...message, type: 'sent', id: state.messages.length});
        state.inputMessage = '';
        scrollToBottom();
      } catch (error) {
        console.error("消息发送失败", error);
      }
    }

    function scrollToBottom() {
      nextTick(() => {
        const chatHistory = this.$refs.chatHistory;
        chatHistory.scrollTop = chatHistory.scrollHeight;
      });
    }

    return {...toRefs(state), setUsername, sendMessage, scrollToBottom};
  }
}
</script>


<style>
/* Modal Styles */
.system-message {
  width: 100%;
  text-align: center;
  padding: 10px 0;
  color: #888; /* 系统消息的字体颜色 */
}

.chat-window {
  margin-left: 300px;
}

.chat-box {
  box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.2);
  position: absolute;
  /*margin: auto;*/
  top: 90px;
  /*width: 100%;*/
  /*height: 80%;*/
  border-radius: 30px;
  overflow: hidden;
  display: flex;
  flex-direction: column;
  width: 50%;
  height: 70%;
  margin-top: 30px;
  margin-left: 210px;
  margin-bottom: 30px;


}


.chat-history {
  flex: 1;
  overflow-y: auto;
  padding: 10px;
  background-color: #f2f5e9;
}

.message {
  padding: 5px 0;
}

.info {
  font-size: 16px;
  font-weight: bold;
  color: gray;
  margin-bottom: 4px;
}

.align-left {
  width: 100%;
  float: left;
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
  align-items: flex-start;
  font-size: 17px;
  font-weight: bold;

}

.align-right {
  font-weight: bold;
  width: 100%;
  float: right;
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: flex-end;
  align-items: flex-end;
  font-size: 17px;
}

.bubble {

  background-color: #e6e6e6;
  border-radius: 15px;
  padding: 12px;
  display: inline-block;
}

.align-right .bubble {
  font-weight: bold;
  background-color: #007bff;
  color: white;
  font-size: 17px;
}

.chat-input {
  display: flex;
  padding: 10px;
  background-color: #f7f7f7;
  border-top: 1px solid #ccc;
}

.inPutting {
  font-weight: bold;
  font-size: 20px;
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 80px;
  margin-right: 10px;
}

button {
  font-weight: bold;
  padding: 20px 30px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 80px;
  cursor: pointer;
}

button:hover {
  background-color: #0056b3;
}

.online-users {
  margin-top: 30px;
  margin-left: 200px;
  box-shadow: 0 8px 16px 0 rgba(0, 0, 0, 0.2);
  position: absolute;
  top: 90px;
  left: 30px;
  width: 200px;
  height: 65.5%;
  border-radius: 30px;
  background-color: #f7f7f7;
  padding: 20px;
  overflow-y: auto;
}

/*在线用户标题*/
.online-users h3 {
  color: #007bff;
  text-align: center;
  margin-bottom: 20px;
  font-size: 40px;
}


/*列表中 用户名字的小框框*/
.online-users li {
  background-color: #e6e6e6;
  border-radius: 15px;
  padding: 10px;
  font-weight: bold;
  font-size: 25px;
  color: #333;
  text-align: center;
  margin-bottom: 10px;
  margin-left: 60px;
  display: block;
  }


</style>







