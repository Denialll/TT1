<script>
import axios from 'axios';

export default {
    data() {
        return {
            tableDate: [],
            id: 0,
            age: 0,
            count: 1,
            sortDesc: false,
            isShow: false ,
            isButtonDisabled: false
        }
    },
    methods: {
        submitName() {
            for (let i = 0; i < this.tableDate.length; i++){
                if (this.tableDate[i].name === this.name) {
                    this.tableDate[i].count++;
                    return 0;
                }
            }

            axios.post('http://localhost:8080/', { name: this.name })
                .then(response => {
                    this.age = response.data;
                    const Arr = { id: this.id++, name: this.name, age: this.age, count: this.count };
                    this.tableDate.push(Arr);
                })
                .catch(error => {
                    console.log(error);
                });

            if (!this.isButtonDisabled) {
                this.isButtonDisabled = true;
                setTimeout(() => {
                    this.isButtonDisabled = false;
                }, 1000);
            }
        },

        showTable() {
            if (this.name != null) this.isShow = true;
        },

        sortTableByAge() {
            this.sortDesc = !this.sortDesc;
            if (this.sortDesc == true) {
                this.tableDate = this.tableDate.sort((a, b) => a.age - b.age);
            } else {
                this.tableDate = this.tableDate.sort((a, b) => a.id - b.id);
            }
        }
    },
};
</script>


<script setup>
import css from '../Name/Name.module.css';
import Button from "../Controls/Button/Button.vue";
</script>


<template>
    <div :class="css.header">
        <form @submit.prevent="() => submitName()">
            <label for="name">Enter name: </label>
            <input :class="css.input_name" placeholder="Enter..." type="text" id="name" v-model="name" required>
            <Button @click="showTable"  :disabled="isButtonDisabled" type="submit">Send</Button>
        </form>
    </div>

    <div v-if="isShow === true" class="container">

        <h2 class="text-center"> Table names</h2>

        <table class="table table-striped">
            <thead>
                <tr  >
                    <th> Name </th>
                    <th>
                        Age<Button styleType="sortIcon" @click="sortTableByAge"> {{ sortDesc ? '↑' : '↓' }}</Button>
                    </th>
                    <th> Count Of Request </th>
                </tr>
            </thead>
            <tbody>
                <tr v-for="user in tableDate" :key="user.id">        
                    <td :class="css.table_name"> {{ user.name }}</td>
                    <td :class="css.table_name"> {{ user.age }}</td>
                    <td :class="css.table_name"> {{ user.count }}</td>              
                </tr>
            </tbody>
        </table>
    </div>
</template>
