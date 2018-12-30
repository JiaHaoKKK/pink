Vue.use(VueMaterial.default);
var app = new Vue({
    el: '#app',
    data: {
        baseUri: "http://pink.isouth.org:8080/api/v1.0/",
        owner: undefined,
        user: undefined,
        todos: []
    },
    mounted: function () {
        this.fetchUser()
            .then(name => {
                this.owner = name;
                this.user = name;
                this.fetchTodos();
            });
    },
    methods: {
        fetchUser: function () {
            return fetch(this.baseUri + "users/self")
                .then(stream => stream.json())
                .then(data => {
                    return data.name;
                })
                .catch(error => console.error(error));
        },
        fetchTodos: function () {
            fetch(this.baseUri + "owners/" + this.owner + "/todos")
                .then(stream => stream.json())
                .then(data => this.todos = data)
                .catch(error => console.error(error));
        }
    }
});
