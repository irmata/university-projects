<template>
    <div class="home">
      <!-- First row with white background -->
      <div class="first-row" style="background-color: #FFFFFF;">
        <!-- Left item with SVG -->
        <div class="item left">
          <img class="svg-image" src="@/assets/dentist_image.svg" alt="Dental SVG" />
        </div>
        <!-- Right item with text and title -->
        <div class="item right">
          <h2>Your dental care in our hands</h2>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit,
            sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
            Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris
            nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in
            reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p>
        </div>
      </div>

      <!-- Second row -->
      <div class="second-row" style="background-color: #EEF1FA;">
        <!-- Left item with text and title -->
        <div class="item left">
          <h2><span style="color: #2174C1">SmileSlot</span> - Trustworthy services</h2>
          <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit,
            sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.
            Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris
            nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in
            reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur.</p>
        </div>
        <!-- Right item with link texts -->
        <div class="item right">
          <ul>
            <li><a href="/login">Log in</a></li>
            <li><a href="/about">About us</a></li>
          </ul>
        </div>
      </div>
    </div>
</template>

<script>

export default {
  name: 'HomeView'
}

</script>

<style lang="scss">
  .home {
    width: 100%;
  }

  .first-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;

    h2 {
        color: #0C375E;
        font-family: 'Inter', sans-serif;
        font-weight: 700;
        font-size: 24pt;
    }

    p {
        color: #2174C1;
        font-family: 'Open Sans', sans-serif;
        font-weight: 600;
        font-size: 18pt;
    }

  }

  .second-row {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 20px;
    position: static;

    h2 {
        color: #0C375E;
        font-family: 'Inter', sans-serif;
        font-weight: 700;
        font-size: 24pt;
    }

    p {
        color: #353535;
        font-family: 'Open Sans', sans-serif;
        font-weight: 400;
        font-size: 14pt;}
  }

  .item {
    flex-basis: 50%;
  }

  .svg-image {
    margin: -25px;
    margin-right: 80px;
  }

  .item.right ul {
    list-style: none;
    padding-left: 100px;
    margin-top: 50px;
  }

  .item.right ul li {
    text-decoration: none;
    padding-bottom: 10px;

    a {
        color: #1A5B97;
        text-decoration: none;
    }
  }

</style>
