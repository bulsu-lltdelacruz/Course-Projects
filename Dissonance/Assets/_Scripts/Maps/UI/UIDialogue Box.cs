using System;
using Unity.VisualScripting;
using UnityEngine;
using UnityEngine.UIElements;

public class UIDialogueBox : MonoBehaviour
{
    //wag na alalahanin na isang instance lang, isa lang talaga iibahin lang text
    static UIDocument UIDOC;
    static VisualElement root;
    static VisualElement parent;
    
    static VisualElement dialougueBox;
    static VisualElement choicesParent;
    static Label dialogue;
    static string[] dialogueContents;
    static Texture2D[] imageContents;
    static Button[] buttonChoices;
    static int counter = 0;
    static Player player;
    static MobileControls mc;
    public static bool IsReady => parent != null && dialogue != null;
    public static void Initialize(Player playerReference)
    {
        player = playerReference;        
    }

    void Start()
    {
        UIDOC = GetComponent<UIDocument>();

        root = UIDOC.rootVisualElement;
        parent = root.Q<VisualElement>("parent");
        dialougueBox = parent.Q<VisualElement>("dialogue-box");
        dialogue = dialougueBox.Q<Label>("dialogue");
        choicesParent = parent.Q<VisualElement>("choices");
        dialogue.text = "helloo";
        parent.RegisterCallback<ClickEvent>(CloseDialogue);
        
        mc = FindAnyObjectByType<MobileControls>();

        parent.visible = false;
        choicesParent.visible = false;
    }
    private static void CloseDialogueIntro(ClickEvent callback = null)
    {
        if(dialogueContents == null)
        {
            SetUIState(false);
            UIImage.ClosePhoto(null);
            return;
        }
        counter++;
        if(counter == dialogueContents.Length-1)//add choice on when the choice is displayed
        {
            choicesParent.Clear();
            if(buttonChoices != null &&buttonChoices.Length > 0)
            {
                choicesParent.visible = true;
                for (int i = 0; i < buttonChoices.Length; i++)
                {
                    choicesParent.Add(buttonChoices[i]);
                }
                parent.UnregisterCallback<ClickEvent>(CloseDialogueIntro);
            }
            
        }
        if(counter >= dialogueContents.Length)
        {
            Debug.Log("inIntro");
            SetUIState(false);
            UIImage.ClosePhoto(null);
        }
        else
        {
            dialogue.text = dialogueContents[counter];
            if(imageContents== null)
                return;
            if(counter >= imageContents.Length)
                return;
            if(imageContents[counter] ==null)
                return;
            UIImage.ClosePhotoFillScreen(null);
            UIImage.OpenPhoto(imageContents[counter]);
            
        }
    }
    private static void CloseDialogue(ClickEvent callback = null)
    {
        if(dialogueContents == null)
        {
            Debug.Log("bye");
            if(Initializer.isIntro)
            {
                Initializer.StartNewGame();
            }
            SetUIState(false);
            UIImage.ClosePhoto(null);
            return;
        }
        counter++;
        if(counter == dialogueContents.Length-1)//add choice on when the choice is displayed
        {
            choicesParent.Clear();
            if(buttonChoices != null &&buttonChoices.Length > 0)
            {
                choicesParent.visible = true;
                for (int i = 0; i < buttonChoices.Length; i++)
                {
                    choicesParent.Add(buttonChoices[i]);
                }
                parent.UnregisterCallback<ClickEvent>(CloseDialogue);
            }
            
        }
        if(counter >= dialogueContents.Length)
        {
            Debug.Log("bye");
            if(Initializer.isIntro)
            {
                Initializer.StartNewGame();
            }
            SetUIState(false);
            UIImage.ClosePhoto(null);
        }
        else
        {
            dialogue.text = dialogueContents[counter];
            if(imageContents== null)
                return;
            if(counter >= imageContents.Length)
                return;
            if(imageContents[counter] ==null)
                return;
            UIImage.ClosePhoto(null);
            UIImage.OpenPhoto(imageContents[counter]);
            
        }
    }

    private static void SetUIState(bool isVisible)
    {
        parent.visible = isVisible;
        choicesParent.visible = isVisible;
        buttonChoices = null;
        if(mc!=null)
            mc.AddControls();
        if (isVisible) PlayerInputs.Player.Disable(); else PlayerInputs.Player.Enable();
        
        if (!isVisible)
        {
            dialogueContents = null;
            imageContents = null;
            counter = 0;
        }
    }

    
    public static void OpenDialogue(string[] content, Texture2D[] images)
    {
        if(content.Length<=0)
        {
            CloseDialogueIntro();
            return;
        }
        buttonChoices = null;
        counter = 0;
        dialogueContents = content;
        imageContents = images;
        dialogue.text = dialogueContents[counter];
        UIImage.ClosePhoto(null);
        if (imageContents != null && imageContents.Length > 0 && imageContents[counter] != null)
            UIImage.OpenPhoto(imageContents[counter]);
        
        if(counter >= dialogueContents.Length)
        {
            CloseDialogueIntro();
        }
        
        parent.visible = true;
        PlayerInputs.Player.Disable();
    }
    public static void OpenDialogue(string[] content)
    {
        if(content.Length<=0)
        {
            CloseDialogue();
            return;
        }
        buttonChoices = null;
        counter = 0;
        dialogueContents = content;
        imageContents = null;
        dialogue.text = dialogueContents[counter];
        UIImage.ClosePhoto(null);
        if(mc!=null)
            mc.RemoveControls();

        if(counter >= dialogueContents.Length)
        {
            CloseDialogue();
        }
        
        parent.visible = true;
        PlayerInputs.Player.Disable();
    }
    
    public static void OpenDialogue(string[] content, Choices[] choices, string[] choicesString, string[] endingDialoguePositive,string[] endingDialogueNegative, Texture2D[] images, HandleItemInteraction itemInteraction)
    {
        if(content.Length<=0)
        {
            CloseDialogue();
            return;
        }
        counter = 0;
        dialogueContents = content;
        dialogue.text = dialogueContents[counter];
        imageContents = images;
        UIImage.ClosePhoto(null);
        if(imageContents != null && imageContents.Length > 0 && imageContents[counter] != null)
            UIImage.OpenPhoto(imageContents[counter]);

        if(counter >= dialogueContents.Length)
            CloseDialogue();

        if(mc!=null)
            mc.RemoveControls();

        parent.visible = true;
        choicesParent.visible = true;
        buttonChoices = new Button[choicesString.Length];
        for (int i = 0; i < choicesString.Length; i++)
        {
            buttonChoices[i] = CreateChoicesButton(choicesString[i], endingDialoguePositive, endingDialogueNegative, choices[i], null, itemInteraction);
        }

        PlayerInputs.Player.Disable();
    }
    public static void OpenDialogue(string[] content, Choices[] choices, string[] choicesString, string[] endingDialoguePositive,string[] endingDialogueNegative, Texture2D[] images, Door doorInRange, HandleItemInteraction itemInteraction)
    {
        if(content.Length<=0)
        {
            CloseDialogue();
            return;
        }
        counter = 0;
        dialogueContents = content;
        dialogue.text = dialogueContents[counter];
        imageContents = images;
        
        if(mc!=null)
            mc.RemoveControls();

        UIImage.ClosePhoto(null);
        if(imageContents != null && imageContents.Length > 0 && imageContents[counter] != null)
            UIImage.OpenPhoto(imageContents[counter]);

        if(counter >= dialogueContents.Length)
            CloseDialogue();

        parent.visible = true;
        choicesParent.visible = true;
        buttonChoices = new Button[choicesString.Length];
        for (int i = 0; i < choicesString.Length; i++)
        {
            buttonChoices[i] = CreateChoicesButton(choicesString[i], endingDialoguePositive, endingDialogueNegative, choices[i], doorInRange, itemInteraction);
        }

        PlayerInputs.Player.Disable();
    }
    public static void OpenDialogue(string[] content, Texture2D[] images, string s)
    {
        if(content.Length<=0)
        {
            CloseDialogue();
            return;
        }

        if(mc!=null)
            mc.RemoveControls();

        buttonChoices = null;
        counter = 0;
        dialogueContents = content;
        imageContents = images;
        dialogue.text = dialogueContents[counter];
        UIImage.ClosePhotoFillScreen(null);
        if (imageContents != null && imageContents.Length > 0 && imageContents[counter] != null)
            UIImage.OpenPhotoFillScreen(imageContents[counter]);
        
        if(counter >= dialogueContents.Length)
        {
            CloseDialogueIntro();
        }
        
        parent.visible = true;
        PlayerInputs.Player.Disable();
    }
    public static void OpenDialogue(string[] content, Choices[] choices, string[] choicesString, string[] endingDialoguePositive,string[] endingDialogueNegative, Texture2D[] images, Door doorInRange, HandleItemInteraction itemInteraction, string s)
    {
        if(content.Length<=0)
        {
            CloseDialogueIntro();
            return;
        }
        counter = 0;
        dialogueContents = content;
        dialogue.text = dialogueContents[counter];
        imageContents = images;
        
        if(mc!=null)
            mc.RemoveControls();

        UIImage.ClosePhotoFillScreen(null);
        if(imageContents != null && imageContents.Length > 0 && imageContents[counter] != null)
            UIImage.OpenPhotoFillScreen(imageContents[counter]);

        if(counter >= dialogueContents.Length)
            CloseDialogueIntro();

        parent.visible = true;
        choicesParent.visible = true;
        buttonChoices = new Button[choicesString.Length];
        for (int i = 0; i < choicesString.Length; i++)
        {
            buttonChoices[i] = CreateChoicesButton(choicesString[i], endingDialoguePositive, endingDialogueNegative, choices[i], doorInRange, itemInteraction);
        }

        PlayerInputs.Player.Disable();
    }

    private static Button CreateChoicesButton(string text, string[] endingDialoguePositive,string[] endingDialogueNegative, Choices choice, Door doorInRange, HandleItemInteraction itemInteraction)
    {
        Button button = new Button();
        button.text = text;
        button.AddToClassList("choicesButton");
        button.RegisterCallback<MouseEnterEvent>(e=>{player.playerSounds.PlayHoverButton(S_VolumeStats.buttonHover);});

        if(choice == Choices.pickUp)
        {
            button.RegisterCallbackOnce<ClickEvent>((ClickEvent e) =>
            {
                choicesParent.Clear();
                e.StopPropagation();

                bool success = false;

                if(doorInRange != null && doorInRange is AnimalPuzzle animalPuzzle)
                {
                    if(itemInteraction.AddItemToInventory(animalPuzzle.keyInSlot))
                    {
                        animalPuzzle.RemoveKeyInSlot();
                        success = true;
                    }else
                    {
                        OpenDialogue(new string[]{"Inventory can't fit all items."});
                    }
                }
                else
                {
                    success = itemInteraction.TryAddItemToInventory();
                }

                if (!success)
                {
                    parent.RegisterCallback<ClickEvent>(CloseDialogue);
                    return;
                }

                OpenDialogue(endingDialoguePositive);
                parent.RegisterCallback<ClickEvent>(CloseDialogue);
            });
        }
        else if(choice == Choices.leave)
        {
            button.RegisterCallbackOnce<ClickEvent>((ClickEvent e) =>
            {
                choicesParent.Clear();
                e.StopPropagation();
                OpenDialogue(endingDialogueNegative);
                parent.RegisterCallback<ClickEvent>(CloseDialogue);
            });
        }
        return button;
    }
}

public enum Choices
{
    pickUp,
    leave
}
