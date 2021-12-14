<script lang="ts">
    import {createEventDispatcher} from 'svelte';

    const dispatch = createEventDispatcher();

    export let module: string;
    export let page: string;
    export let active: boolean = true;
    export let indented: boolean = true;

    let click = () => {
        dispatch("onclick", {module: module, page: page});
    }
</script>


<div class="flex py-2 {active ? 'bg-gray-700' : 'bg-gray-900'} {active ? 'hover:cursor-pointer' : ''} { indented ? 'pl-4' : 'pl-2' } hover:bg-gray-900 transition-all" on:click="{click}">
    <h3 class="text-xl px-2">{page}</h3>
</div>