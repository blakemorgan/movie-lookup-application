<template>
    <div id="search">
        <div id="search-box">
            <input type="search" placeholder="Search movies..." v-model="searchInputValue" name="search"
                   id="search-input" @keyup.enter="runSearch"/><br>
            <button id="search-submit" @click="runSearch" type="submit">Search</button>
        </div>
        <div v-if="loading">
            <img src="../assets/ajax-loader.gif"/>
            Loading...
        </div>
        <div id="search-results">
            <!-- eslint-disable-next-line -->
            <div v-for="movie in data" class="movie">
                <img :src="movie.poster_image_url" :alt="movie.title" class="movie-poster"/>
                <h2>{{ movie.title }}</h2>
                <p>Popularity: {{ movie.popularity_summary }}</p>
            </div>
        </div>
    </div>
</template>

<script>
    import axios from 'axios';

    export default {
        name: "Search",
        methods: {
            runSearch() {
                this.loading = true
                axios
                    .get("//localhost:8081/movies?search=" + this.searchInputValue)
                    .then((response) => {
                        this.loading = false
                        this.data = response.data
                    })
            }
        },
        data: function () {
            return {
                searchInputValue: '',
                data: null,
                loading: false
            }
        }
    }
</script>

<style scoped>
    #search-input {
        height: 30px;
        width: 250px;
        border-radius: 30px;
        border: solid 1px #e4e4e4;
        padding: 0 10px;
    }
    #search-submit {
        margin: 10px;
        border: solid 1px #e4e4e4;
        height: 30px;
        width: 100px;
        border-radius: 30px;
    }
    #search-submit:active {
        background-color: #d2d2d2;
    }
    .movie {
        border-top: solid 1px #e4e4e4;
        max-width: 600px;
        margin: 0 auto;
        padding: 20px 0;
        height: 200px;
    }
    .movie h2 {
        margin: 0;
    }
    .movie-poster {
        margin: 0 10px 15px;
        float: left;
    }
</style>