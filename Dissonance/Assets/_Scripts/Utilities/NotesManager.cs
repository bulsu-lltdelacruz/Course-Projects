using UnityEngine;
using UnityEngine.UIElements;

public class NotesManager : MonoBehaviour, IDataPersistence
{
    [SerializeField] private UIDocument uiDocument;

    public TextField notesField;
    private Button exitButton;
    private VisualElement parent;
    public static bool isOpen = false;
    private string currentNotes = "";
    private bool isInitialized  = false;
    public static bool isFocused;

    void Start()
    {
        var root  = uiDocument.rootVisualElement;
        parent    = root.Q<VisualElement>("parent");
        notesField = root.Q<TextField>();
        exitButton = root.Q<Button>("exit");

        if (notesField != null)
        {
            notesField.value = currentNotes;
            notesField.RegisterValueChangedCallback(OnNotesChanged);
            notesField.RegisterCallback<KeyDownEvent>(e => { if (e.keyCode == KeyCode.Tab) e.PreventDefault(); });

            notesField.RegisterCallback<FocusInEvent>(e => {isFocused = true;});
            notesField.RegisterCallback<FocusOutEvent>(e => {isFocused = false;});
        }

        if (exitButton != null)
            exitButton.clicked += Close;
        parent.AddToClassList("parent-close");
        isInitialized = true;
    }

    void OnDestroy()
    {
        if (notesField != null)
            notesField.UnregisterValueChangedCallback(OnNotesChanged);
        if (exitButton != null)
            exitButton.clicked -= Close;
    }

    public void Open()
    {
        notesField?.Blur();
        if (parent != null)
            parent.RemoveFromClassList("parent-close");
            
        notesField.isReadOnly = false;
        MobileControls mc = FindAnyObjectByType<MobileControls>();
        if(mc!=null)
            mc.AddControls();

        isOpen = true;
        PlayerInputs.Player.Disable();
        PlayerInputs.UI.Enable();
        Time.timeScale = 0f;
    }

    public void Close()
    {
        Debug.Log("close notes");
        notesField?.Blur();
        notesField.isReadOnly = true;
        if (parent != null)
            parent.AddToClassList("parent-close");

        MobileControls mc = FindAnyObjectByType<MobileControls>();
        if(mc!=null)
            mc.AddControls();

        isOpen = false;
        PlayerInputs.UI.Disable();
        PlayerInputs.Player.Enable();
        FindAnyObjectByType<Player>().input.isUI = false;
        Time.timeScale = 1f;
    }


    private void OnNotesChanged(ChangeEvent<string> evt)
    {
        currentNotes = evt.newValue;
    }

    public void SaveData(ref GameData data)
    {
        data.playerNotes = currentNotes;
    }

    public void LoadData(GameData data)
    {
        currentNotes = data.playerNotes ?? "";
        if (isInitialized && notesField != null)
            notesField.value = currentNotes;
    }
}