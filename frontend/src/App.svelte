<script lang="ts">
  import { Route, Router } from "svelte-routing";
  import Navbar from "./lib/components/Navbar.svelte";
  import Sidemenu from "./lib/components/Sidemenu.svelte";
  import Home from "./lib/pages/Home.svelte";
  import { fly } from "svelte/transition";
import Page from "./lib/pages/Page.svelte";

  let expandedMenu = false;

  let openMenu = () => {
    expandedMenu = true;
  };

  let closeMenu = () => {
    expandedMenu = false;
  };
</script>

{#if expandedMenu}
  <!-- Need z-index because of drop-shadow on nav-bar -->
  <div
    transition:fly={{ x: -100, duration: 300 }}
    class="flex lg:hidden fixed inset-0 z-10"
  >
    <Sidemenu on:closemenu={closeMenu} />
    <div
      class="flex-1  backdrop-blur-sm hover:cursor-pointer"
      on:click={closeMenu}
    />
  </div>
{/if}
<main>
  <div class="h-screen flex flex-col">
    <Navbar on:openmenu={openMenu} />
    <Router>
      <div class="flex h-full">
        <div class="hidden lg:block h-full">
          <Sidemenu />
        </div>
        <div class="flex-1 bg-gray-600 h-full">
          <Route path="/" component={Home} />
          <Route path="/:modulename/:pagename" component={Page} />
        </div>
      </div>
    </Router>
  </div>
</main>

<style lang="postcss" global>
  @tailwind base;
  @tailwind components;
  @tailwind utilities;
</style>
