import {createRouter, createWebHistory} from "vue-router";
import PkIndexView from "../view/pk/PkIndexView";
import RankListIndexView from "../view/ranklist/RankListIndexView";
import RecordIndexView from "../view/record/RecordIndexView";
import UserBotIndexView from "../view/user/bot/UserBotIndexView";
import ErrorIndexView from "../view/error/ErrorIndexView";
import UserAccountLoginView from "@/view/user/account/UserAccountLoginView";
import UserAccountRegisterView from "@/view/user/account/UserAccountRegisterView";
import store from '@/store/index'

const routes = [
    {
        path: "/",
        name:"home",
        redirect: "/pk/",
        meta:{
            requestAuth:true,
        }
    },
    {
        path: "/pk/",
        name: "pk_index",
        component: PkIndexView,
        meta:{
            requestAuth:true,
        }
    },
    {
        path: "/record/",
        name: "record_index",
        component: RecordIndexView,
        meta:{
            requestAuth:true,
        }
    },
    {
        path: "/user/bot/",
        name: "user_bot_index",
        component: UserBotIndexView,
        meta:{
            requestAuth:true,
        }
    },
    {
        path: "/404/",
        name: "404",
        component: ErrorIndexView,
        meta:{
            requestAuth:false,
        }
    },
    {
        path: "/ranklist/",
        name: "ranklist_index",
        component: RankListIndexView,
        meta:{
            requestAuth:true,
        }
    },
    {
        path: "/:catchAll(.*)",
        redirect: "/404/"
    },
    {
        path: "/user/account/login/",
        name: "user_account_login",
        component: UserAccountLoginView,
        meta:{
            requestAuth:false,
        }
    },
    {
        path: "/user/account/register/",
        name: "user_account_register",
        component: UserAccountRegisterView,
        meta:{
            requestAuth:false,
        }
    }
]

const router = createRouter({
    history: createWebHistory(),
    routes
})

router.beforeEach((to,from,next)=>{
    if(to.meta.requestAuth&&!store.state.user.is_login){
        next({name:"user_account_login"});
    }
    else{
        next();
    }
})

export default router