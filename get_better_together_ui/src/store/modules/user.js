import service from "@/utils/axios";

export default {
    namespaced: true,
    state: {
        user1: localStorage.getItem('user1'), // 男
        user2: localStorage.getItem('user2'), // 女
    },
    mutations: {
        SET_USERINFO(state, users) {
            state.user1 = users.user1;
            state.user2 = users.user2;
            localStorage.setItem('user1', users.user1);
            localStorage.setItem('user2', users.user2);
        },
    },
    actions: {
        async getUserInfo({ commit }) {
            const res = await service.get('/user/info');
            commit('SET_USERINFO', {
                user1: res.users[0],
                user2: res.users[1]
            });
            return res;
        },
    },
};