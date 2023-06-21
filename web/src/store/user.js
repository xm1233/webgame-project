import $ from 'jquery'

export default {
    state: {
        id: "",
        username: "",
        photo: "",
        is_login: false,
        token: "",
        show_content:false
    },
    getters: {},
    mutations: {
        updateUser(state, user) {
            state.id = user.id;
            state.username = user.username;
            state.photo = user.photo;
            state.is_login = user.is_login;
        },
        updateToken(state, token) {
            state.token = token;
        },
        log_out(state){
            state.id='';
            state.username='';
            state.photo='';
            state.is_login=false;
            state.token='';
        },
        updateShowContent(state,show_content){
            state.show_content=show_content;
        }
    },
    actions: {
        login(context, data) {
            $.ajax({
                url: "http://localhost:3000/user/account/login/",
                type: "post",
                data: {
                    username: data.username,
                    password: data.password
                },
                success(resp) {
                    if (resp.error_msg === "success") {
                        localStorage.setItem("jwt_token",resp.token);
                        context.commit("updateToken", resp.token);
                        data.success(resp);
                    } else {
                        data.error(resp);
                    }
                },
                error(resp) {
                    data.error(resp);
                }
            });
        },
        getinfo(context,data){
            $.ajax({
              url:"http://localhost:3000/user/account/info/",
              type:"get",
              headers:{
                Authorization:"Bearer "+context.state.token
             },
              success(resp){
                  if(resp.error_msg==="success"){
                      context.commit("updateUser", {
                          ...resp,
                          is_login:true,
                      });
                      data.success(resp);
                  }else{
                      data.error(resp);
                  }
              },
              error(resp){
                data.error(resp);
              }
            }
            );
        },
        log_out(context){
            localStorage.removeItem("jwt_token");
            context.commit("log_out");
        }
    },
    modules: {}
}