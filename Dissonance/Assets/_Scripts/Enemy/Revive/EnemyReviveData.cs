using System;

[Serializable]
public class EnemyReviveData
{
    public string enemyId;
    public bool isDead;
    public bool isBurned;
    public bool willRevive;
    public int doorsUntilRevive;
    public bool hasRevived;
    public float deathPosX;
    public float deathPosY;
    public float deathPosZ;

    public EnemyReviveData(string id)
    {
        enemyId = id;
    }
}