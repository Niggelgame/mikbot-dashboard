<script>
    import { links } from "svelte-routing";
    import { createEventDispatcher } from "svelte";

    const dispatch = createEventDispatcher();

    function openMobileSidemenu() {
        dispatch("openmenu");
    }

    let logout = () => {
        console.log("logout");
    };
</script>

<div class="bg-gray-800 drop-shadow-md">
    <ul class="py-3 px-1 flex h-full" use:links>
        <li class="lg:hidden transition-all">
            <div
                class="my-auto ml-3 text-white hover:text-gray-400"
                on:click={openMobileSidemenu}
            >
                <svg
                    class="fill-current h-8 w-8"
                    viewBox="0 0 20 20"
                    xmlns="http://www.w3.org/2000/svg"
                    ><title>Menu</title><path
                        d="M0 3h20v2H0V3zm0 6h20v2H0V9zm0 6h20v2H0v-2z"
                    /></svg
                >
            </div>
        </li>
        <li
            class="ml-3 mr-3 content col-auto rounded inline-block transition-all hover:bg-gray-700 mx-1 my-auto"
        >
            <a href="/" class="text-white text-xl sm:text-2xl">Dashboard</a>
        </li>
        <li class="flex-1" />
        <li class="mr-3 my-auto">
            <button
                class="inline-block border border-blue-500 rounded py-1 px-3 bg-blue-500 text-white hover:bg-blue-600 hover:transition-all"
                on:click={logout}>Logout</button
            >
        </li>
    </ul>
</div>
