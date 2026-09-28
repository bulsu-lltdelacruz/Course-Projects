using System.Collections;
using UnityEngine;
using UnityEngine.SceneManagement;

public class Initializer : MonoBehaviour
{
    CamBehaviorHandler camBehaviorHandler;
    [SerializeField]Player player;
    [SerializeField]PlayerInputs playerInputs;
    [SerializeField] MovementHandler movementHandler;
    [Header("Player Stats")]
    [SerializeField] static PlayerInputs input;
    public static float screenWidth;
    public static float screenHeight;
    public static float renderWidth;
    public static float renderHeight;
    public static float scaleX = 0;
    public static float scaleY = 0;
    public static int loadFileIndex;
    public static bool isInGame = false;
    static string[] pendingNewGameDialogue;
    static Texture2D[] pendingNewGamePhotos;
    public static bool isIntro = false;
    void Awake()
    {
        camBehaviorHandler = GetComponent<CamBehaviorHandler>();
        QualitySettings.vSyncCount = 0;
        #if UNITY_ANDROID
                Application.targetFrameRate = 60;
        #else
                Application.targetFrameRate = -1;
        #endif
    }
    void Start()
    {
        movementHandler.InitComponents(player,  camBehaviorHandler, playerInputs);
        renderWidth= Camera.main.targetTexture.width;
        renderHeight = Camera.main.targetTexture.height;
        scaleX = Screen.width/renderWidth;
        scaleY = Screen.height/renderHeight;
        SceneManager.sceneLoaded+=OnGameSceneLoaded;
        if (HasPendingNewGameIntro())
            StartCoroutine(OpenPendingNewGameIntro());

        if(isIntro)
        {
           // FindAnyObjectByType<MobileControls>().RemoveControls();
        }
        //REMEMBER, SCALE X ISANG BESES LANG NASASABI SO EITHER START IN FULLSCREEN OR UPDATE IT WHEN SCREEEN CHANGES
        //player.InitComponents(enemyInLockInRangeCheck);
    }
    void OnDestroy()
    {
        SceneManager.sceneLoaded-=OnGameSceneLoaded;
    }
    void OnGameSceneLoaded(Scene scene, LoadSceneMode loadMode)
    {
        UIFade.fade.visible = true;
        UIFade.SetOpacity(1f);
        UIImage.ChangePhotoBGAlpha(255);

        if (!HasPendingNewGameIntro())
            StartCoroutine(UIFade.MenuStartDarkToLightTransition());
    }
    void Update()
    {
        if (Camera.main != null && Camera.main.targetTexture != null)
        {
            renderWidth = Camera.main.targetTexture.width;
            renderHeight = Camera.main.targetTexture.height;
        }

        if (renderWidth > 0 && renderHeight > 0)
        {
            scaleX = Screen.width / renderWidth;
            scaleY = Screen.height / renderHeight;
        }
       // scaleX = Screen.width/renderWidth;
        //scaleY = Screen.height/renderHeight;
       // Debug.Log(scaleX);
    }

    public static void QueueNewGameIntro(string[] dialogueContents, Texture2D[] imageContents)
    {
        if (dialogueContents == null || dialogueContents.Length == 0)
        {
            pendingNewGameDialogue = null;
            pendingNewGamePhotos = null;
            return;
        };
        isIntro = true;
        pendingNewGameDialogue = (string[])dialogueContents.Clone();
        pendingNewGamePhotos = imageContents != null ? (Texture2D[])imageContents.Clone() : null;
    }

    static bool HasPendingNewGameIntro()
    {
        return pendingNewGameDialogue != null && pendingNewGameDialogue.Length > 0;
    }

    static void ConsumePendingNewGameIntro(out string[] dialogueContents, out Texture2D[] imageContents)
    {
        dialogueContents = pendingNewGameDialogue;
        imageContents = pendingNewGamePhotos;
        pendingNewGameDialogue = null;
        pendingNewGamePhotos = null;
    }

    IEnumerator OpenPendingNewGameIntro()
    {
        UIFade.fade.visible = true;
        UIFade.SetOpacity(1f);

        yield return null;

        while (HasPendingNewGameIntro() &&
            (FindAnyObjectByType<PlayerInputs>() == null || !UIDialogueBox.IsReady || !UIImage.IsReady))
        {
            yield return null;
            player.transform.position = Vector3.zero;
        }

        if (!HasPendingNewGameIntro())
        {
            StartCoroutine(UIFade.StartDarkToLightTransition());
            UIImage.ChangePhotoBGAlpha(0);
            yield break;
        }

        ConsumePendingNewGameIntro(out string[] dialogueContents, out Texture2D[] imageContents);

        UIDialogueBox.OpenDialogue(dialogueContents, imageContents, "");

        StartCoroutine(UIFade.StartDarkToLightTransition());
    }
    public static void StartNewGame()
    {
        isIntro = false;
        FindAnyObjectByType<Player>().transform.position = Vector3.zero;
        MobileControls mc = FindAnyObjectByType<MobileControls>();
        if(mc!=null)
            mc.AddControls();
    }
}
