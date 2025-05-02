Android Photo App - How to Use

1. When you open the app, you'll see a list of albums.
2. Tap the "+" button to create a new album.
3. Tap an album to view the photos inside it.
4. Inside an album:
   * Tap the "+" button to add a photo from your device.
   * Long press a photo to delete it.
   * Tap a photo to open the slideshow.

5. In slideshow view:
   * Use "Next" and "Previous" to go through photos.
   * Tap "Add Tag" to add a person or location tag.
   * Tap "Delete Tag" to remove a tag.
   * Tap "Move Photo" to move the photo to another album.
   * Use the back button to return to the album.

6. To rename or delete an album, long press it from the home screen.

7. Usage of GenAI
   * What prompts were issued 
      - These are just some of the prompts that were issued when developing the project, but it provides a good overview
        of how the project was developed piece-by-piece and step-by-step while thoroughly testing and ensuring edge cases
        are accounted for. We provided detailed inputs to the chatbot and as a result we were given the output according
        to it. This way, the chatbot is less confused, gives us exactly what we want, and does not provide us garbage code.
        As long as you interact with it, it will work fine. This helped us save a lot of time.
      - First, we issued prompts to help us setup the environment and how to get started with developing the app. After 
        the environment was set up, we started with developing and providing the current project structure and functionalities 
        we have. For example, we provided the following input:
      - "Ok, I was able to run the app and open the application. I ran the build and 
        was able to open the app. I was able to interact and test some of the functionality my partner implemented 
        so far. I check the project configuration(step 3) and its looking good. Let me give you a list of the 
        functionalities that has been implemented so far: Home screen shows albums, Add new album, Save/load albums 
        persistently, Click album to open photos, Add photo to album, Save/load photos in albums, Delete photo, 
        SlideShow view. Give me a moment to provide you with the project structure we have so far. If I give you a 
        screenshot of it, will you be able to understand the structure? Or would you want me to give you a written 
        text structure?"
      - "Tagging functionality has been implemented by my partner. Now, I will work on search and then auto complete 
        functionalities and I will need your help in implementing this functionality. First, lets get started with 
        developing the UI and then we will work on developing the code based on the UI I edit and finalize. For the code,
        first we will implement searching and then the auto-complete feature."

        **** If the chatbot was not following the requirements/preferences, we informed the chatbot corrected it. For example,
        it was implementing searching in home screen instead of album screen. 
      - "Actually, the user should be able to search for photos after he/she opens an album. Once the user opens an album, 
        they will be able to search. The search results should, however, include photos from the album that is open as 
        well as all other albums. With this requirement in consideration, lets make the necessary changes"
        When testing for edge cases, we often found that that chatbot did not account for edge cases which resulted in
        bugs. So, we had to inform the chatbot about it so that edge cases are accounted for. 
      - "There are some bugs I would like you to fix:
         1. When I go to search without any input tags, all the photos appear from all albums regardless of whether its 
            conjunction or disjunction. Since there is no input, no photos should appear.
         2. When I search with 1 person tag and use "OR", the photo with that tag and all other photos from other albums appear.
         3. When I search with 2 person tags using "OR", with one person tag has the full name entered and the 2nd person 
            tag has partial name entered, the photo with the partial tag name also appears. The photo with the partial tag 
            name should not appear. For example, 1st person tag(full name): Jessica, 2nd person tag(partial name): ma   
            (full name is mary, for reference). In this case, photos tagged with Jessica appears as well as photos tagged 
            with mary. Photos tagged with Jessica is correctly displayed since the entire matching name is entered. however, 
            there is no photos with tag value "ma", so photos tagged with mary should not appear.
         Also, assume that other test cases work."
      - "Great, that works! Now, there is one minor issue that needs to be fixed and we will be all set for the 
         auto-complete and search functionality. Let's consider that I have a location tag saved as "New York" for 
         certain photo and for another photo, I have assigned the location tag as "York". When I search by location 
         for "York", both tags, New York and York, come up as suggestions. However, the ordering of the list is that 
         "New York" is first in the list and "York" is second in the list, but "York" should be first in the list since 
         it is the closest/exact match to what I entered-"York"- and then "New York" should come at the 2nd place in 
         the list."

   * What UI drawings you uploaded to the chatbot
      - We did not upload/gave any UI drawings to the chatbot. Instead, we asked the chatbot to generate a 
        template for us. In the template, we asked the chatbot to include certain components that were required 
        or the ones that met our preferences. Even after giving proper instructions, we still had to make 
        adjustments and changes. We did that using the Layout Editor/UI Designer that is in-built with the ide.
   * Which of the code components were generated by the AI
      - In terms of functionality, almost all of the code components were developed by AI. We developed the
        functionalities piece-by-piece. First, we developed the home screen that displays, opens, creates & 
        deletes the albums. Then, album screen was developed which displays, adds & deletes the photos. 
        Then, the slideshow view was developed along with the functionalities that came with it such as managing 
        tags, moving photos, and previous/next photos. 
      - Furthermore, we also asked chatbot to list out the steps we should take when developing this project. 
        Some steps were followed, while some were not. For example, chatbot recommended us to implement 
        tagging -> searching -> slideshow functionalities in order. But we implemented slideshow before tagging 
        and searching since it was easier to do.
   * Which code components were written by us
      - The code written by us involved when there was an issue or a bug. Most of the bugs/fixes were about 
        updating the screens once an action was taken by the user such as deleting and moving photos & albums.
