import { RouterName } from "./router-name";


// 主要是菜单，比如首页看到的那些页面按钮
export const enum NavName {
  Home = "首页",
  SongSheet = "歌单",
  Singer = "歌手",
  Personal = "个人主页",
  Setting = "设置",
  SignIn = "登录",
  SignUp = "注册",
  SignOut = "退出",
  ChatRoom='聊天室',
}

// 左侧导航栏
export const HEADERNAVLIST = [
  {
    name: NavName.Home,
    path: RouterName.Home,
  },
  {
    name: NavName.SongSheet,
    path: RouterName.SongSheet,
  },
  {
    name: NavName.Singer,
    path: RouterName.Singer,
  },
  {
    name: NavName.ChatRoom,
    path: RouterName.ChatRoom,
  },
];

// 右侧导航栏
export const SIGNLIST = [
  {
    name: NavName.SignIn,
    path: RouterName.SignIn,
  },
  {
    name: NavName.SignUp,
    path: RouterName.SignUp,
  },
];

// 用户下拉菜单项
export const MENULIST = [
  {
    name: NavName.Personal,
    path: RouterName.Personal,
  },
  {
    name: NavName.Setting,
    path: RouterName.Setting,
  },
  {
    name: NavName.SignOut,
    path: RouterName.SignOut,
  },
];
