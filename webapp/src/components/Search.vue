<template>
    <div id="search">
        <div id="search-box">
            <input type="search" placeholder="Search movies..." v-model="searchInputValue" name="search"
                   id="search-input"/>
            <button id="search-submit" @click="runSearch">Search</button>
        </div>
        <div id="search-results">
            <!-- eslint-disable-next-line -->
            <div v-for="movie in data" class="movie">
                {{ movie.title }}<br>
                <img :src="movie.poster_image_url" :alt="movie.title" /><br>
                Popularity: {{ movie.popularity_summary }}
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
                axios
                    .get("//localhost:8081/movies?search=" + this.searchInputValue)
                    .then(response => (this.data = response.data))
            }
        },
        data: function () {
            return {
                searchInputValue: '',
                data: null
            }
        }
    }
</script>

<style scoped>

</style>