public class EnemyStateMachine
{
    public EnemyState currentState;
    public EnemyState previousState;

    public void Initialize(EnemyState startingState)
    {
        previousState = startingState;
        currentState = startingState;
        if(currentState != null)
            currentState.EnterState();
    }
    public void ChangeState(EnemyState newState)
    {
        previousState = currentState;
        if(previousState is EnemyDieState)
            return;
        if (newState != null)
        {
            if(currentState != null)
                currentState.ExitState();
            currentState = newState;
            currentState.EnterState();
        }
        
    }
    public void ChangeStateFromRevive(EnemyState newState)
    {
        previousState = currentState;
        if (newState != null)
        {
            if(currentState != null)
                currentState.ExitState();
            currentState = newState;
            currentState.EnterState();
        }
        
    }
    public void EnterStateInstantDeath(EnemyState newState)
    {
        previousState = currentState;
        if (newState != null)
        {
            if(currentState != null)
                currentState.ExitState();
            currentState = newState;
            currentState.EnterState();
        }
        
    }
    public void ChangeState(EnemyDieState dieState)
    {
        previousState = currentState;
        if (dieState != null)
        {
            if(currentState != null)
                currentState.ExitState();
            currentState = dieState;
            currentState.EnterState();
        }
        
    }
}