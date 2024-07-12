import axios from "axios";
import router from "@/router";

const BASE_URL = process.env.NODE_HOST;

axios.defaults.timeout = 5000; // 超时时间设置
axios.defaults.withCredentials = true; // true允许跨域
axios.defaults.baseURL = BASE_URL;
// Content-Type 响应头
axios.defaults.headers.post["Content-Type"] = "application/x-www-form-urlencoded;charset=UTF-8";

// 响应拦截器
axios.interceptors.response.use(
  (response) => {
    // 如果返回的状态码为200，说明接口请求成功，可以正常拿到数据
    // 否则的话抛出错误
    if (response.status === 200) {
      return Promise.resolve(response);
    } else {
      return Promise.reject(response);
    }
  },
  // 服务器状态码不是2开头的的情况
  (error) => {
    if (error.response.status) {
      switch (error.response.status) {
        // 401: 未登录
        case 401:
          router.replace({
            path: "/",
            query: {
              // redirect: router.currentRoute.fullPath
            },
          });
          break;
        case 403:
          // console.log('管理员权限已修改请重新登录')
          // 跳转登录页面，并将要浏览的页面fullPath传过去，登录成功后跳转需要访问的页面
          setTimeout(() => {
            router.replace({
              path: "/",
              query: {
                // redirect: router.currentRoute.fullPath
              },
            });
          }, 1000);
          break;

        // 404请求不存在
        case 404:
          // console.log('请求页面飞到火星去了')
          break;
      }
      return Promise.reject(error.response);
    }
  }
);

export function getBaseURL() {
  return BASE_URL;
}

/**
 * 封装get方法
 * @param url
 * @param data
 * @returns {Promise}
 */
export function get(url, params?: object) {
  return new Promise((resolve, reject) => {
    axios.get(url, params).then(
      (response) => resolve(response.data),
      (error) => reject(error)
    );
  });
}

/**
 * 封装post请求
 * @param url
 * @param data
 * @returns {Promise}
 */
export function post(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.post(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error)
    );
  });
}

/**
 * 封装delete请求
 * @param url
 * @param data
 * @returns {Promise}
 */
export function deletes(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.delete(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error)
    );
  });
}

/**
 * 封装put请求
 * @param url
 * @param data
 * @returns {Promise}
 */
export function put(url, data = {}) {
  return new Promise((resolve, reject) => {
    axios.put(url, data).then(
      (response) => resolve(response.data),
      (error) => reject(error)
    );
  });
}

//
// 这段代码是一个简单的Axios请求封装，用于处理前端与后端的通信。
//
// 引入了 Axios 库和路由器 router。
// 设置了基础 URL，该 URL 从环境变量中获取。
// 设置了 Axios 的一些默认配置，如超时时间、跨域配置和基础 URL。
// 设置了 POST 请求的 Content-Type 头部为 "application/x-www-form-urlencoded;charset=UTF-8"。
// 添加了响应拦截器，用于处理接口请求成功和失败的情况。
// 如果状态码为 200，则表示请求成功，返回数据。
// 如果状态码不是 2 开头，则根据不同状态码进行相应处理，如未登录、权限错误、请求不存在等。
// 定义了一个函数 getBaseURL() 用于获取基础 URL。
// 封装了四种请求方法：get、post、delete、put。
// get 方法用于发送 GET 请求。
// post 方法用于发送 POST 请求。
// delete 方法用于发送 DELETE 请求。
// put 方法用于发送 PUT 请求。
// 这些封装的请求方法都返回 Promise 对象，可以通过调用这些方法来发送相应类型的请求，并在 Promise 的 resolve 和 reject 中处理请求成功和失败的情况。
//
// 总体来说，这段代码实现了对 Axios 的封装，使得前端可以方便地发送不同类型的请求，并对请求的响应进行统一处理和拦截。
//
// Ask Claude 3 Opus
