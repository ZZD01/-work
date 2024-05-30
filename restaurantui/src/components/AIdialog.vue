<template>
  <div class="dialog-container">
      <button class="button" @click="queryHistory">膳食分析与今日推荐</button>
    <div class="dialog">
      <div class="conversation" v-for="(message, index) in conversation" :key="index">
        <div v-if="message.type === 'user'" class="user-message">{{ message.content }}</div>
        <div v-else class="ai-message">{{ message.content }}</div>
      </div>
      
      <div class="input-container">
        <input v-model="userMessage" @keyup.enter="sendMessage" placeholder="请输入您的问题...">
        <button class="button" @click="sendMessage">发送</button>
      </div>
    </div>
  </div>
</template>

<script>
import * as menuApi from "@/base/api/menu.js";
import querystring from "querystring";
import { systemConfig } from "@/base/config/system";
var apiUrl = systemConfig.apiUrl;

export default {
  data() {
    return {
      userMessage: '',
      conversation: [],
      desserts: [],
      list: [],
      menus: [],
      orimenu: [],
      messages: [
        {
          role: 'system',
          content: '你是一个营养分析师',
        },
      ],
    };
  },
  mounted() {
    this.getMenu();
  },
  methods: {
    getMenu() {
      menuApi
        .listAllMenu()
        .then(result => {
          if (result.success) {
            const list = result.data.list;
            for (let j = 0; j < list.length; j++) {
              this.orimenu.push(list[j].name);
            }
            console.log(this.orimenu);
          } else {
            this.$snackbar.error(result.message);
          }
        })
        .catch(error => {
          this.$snackbar.error(error.message);
        });
    },
    async sendMessage() {
      if (!this.userMessage) return;

      // 将用户的消息添加到对话历史中
      this.conversation.push({ type: 'user', content: this.userMessage });

      // 准备发送给 GPT 的对话历史
      const messages = this.messages.concat({ role: 'user', content: this.userMessage });
      console.log(messages);

      try {
        // 调用 GPT API
        const gptResponse = await fetch('api/v1/chat/completions', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer sk-IOC81gCsUjpHLlabXd8ZBy6rT8spWdWkJR8kJlKJtgu8Xlhj', // 替换为你的 API 密钥
          },
          body: JSON.stringify({
            model: "gpt-3.5-turbo",
            messages: messages,
          }),
        });

        if (gptResponse.ok) {
          const gptData = await gptResponse.json();

          // 将 AI 助手的回复添加到对话历史中
          this.conversation.push({ type: 'ai', content: gptData.choices[0].message.content });
          this.messages.push({ role: 'assistant', content: gptData.choices[0].message.content });
        } else {
          console.error('Error:', await gptResponse.text());
          this.$snackbar.error('Failed to fetch response from GPT-3');
        }
      } catch (error) {
        console.error('Error:', error);
        this.$snackbar.error('Failed to fetch response from GPT-3');
      }

      // 清空用户输入框
      this.userMessage = '';
    },
    async queryHistory() {
      try {
        const result = await menuApi.listAllOrderByUserId(this.$root.$data.userId);
        if (result.success) {
          this.desserts = result.data.list;
          console.log(this.desserts);

          const menuNames = [];

          // 使用 Promise.all 并行执行所有订单详细信息请求
          const orderDetailsPromises = this.desserts.map(dessert =>
            menuApi.listOrderDetailsByOrderId(dessert.id)
          );

          const orderDetailsResults = await Promise.all(orderDetailsPromises);

          orderDetailsResults.forEach(orderDetails => {
            if (orderDetails.success) {
              const list = orderDetails.data.list;
              for (let j = 0; j < Math.min(5, list.length); j++) {
                menuNames.push(list[j].menuName);
              }
            } else {
              this.$snackbar.error(orderDetails.message);
            }
          });

          // Prepare data for GPT API
          const question = `这一份菜单名字是用户前五顿饭的所有菜品，请你对这些菜品营养成分进行分析：(${menuNames.join(', ')}) 并且根据膳食营养均衡的需求，从这份菜单中：(${this.orimenu.join(', ')})对用户接下来的膳食菜品给出建议,并选出三道菜作为推荐,字数限制在100字以内`;

          // Send request to GPT API
          const gptResponse = await fetch('/api/v1/chat/completions', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': 'Bearer sk-IOC81gCsUjpHLlabXd8ZBy6rT8spWdWkJR8kJlKJtgu8Xlhj' // 替换为你的 API 密钥
            },
            body: JSON.stringify({
              model: "gpt-3.5-turbo",
              messages: [
                { role: "system", content: "You are a helpful assistant." },
                { role: "user", content: question }
              ],
            })
          });

          const gptData = await gptResponse.json();

          // Display GPT response in conversation
          this.conversation.push({ type: 'ai', content: gptData.choices[0].message.content });

        } else {
          this.$snackbar.error(result.message);
        }
      } catch (error) {
        this.$snackbar.error(error.message);
      }
    }
  }
};
</script>

<style scoped>
.dialog-container {
  position: fixed;
  top: -20px;
  right: 0;
  width: 500px;
  height: 90%;
  background-color: rgba(255, 246, 211, 0.856);
  border-left: 2.5px solid rgb(250, 135, 4);
  overflow-y: auto;
}
.dialog {
  padding: 20px;
}
.conversation {
  margin-bottom: 10px;
}
.user-message {
  text-align: right;
  background-color: #e6ffe6;
  padding: 10px;
  border-radius: 10px;
  margin-bottom: 5px;
  max-width: 50%;
  margin-left: auto;
}
.ai-message {
  text-align: left;
  background-color: #f0f0f0;
  padding: 10px;
  border-radius: 10px;
  margin-bottom: 5px;
  max-width: 80%;
  margin-right: auto;
}
.input-container {
  display: flex;
}
.input-container input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
  margin-right: 5px;
}
.button {
  padding: 8px 15px;
  background-color: #1b89ff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
</style>
