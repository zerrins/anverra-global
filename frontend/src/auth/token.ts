type TokenGetter = () => Promise<string | undefined>;

let tokenGetter: TokenGetter | null = null;
let initializationPromise: Promise<void> | null = null;
let resolveInitialization: (() => void) | null = null;

export const setTokenGetter = (getter: TokenGetter | null) => {
  tokenGetter = getter;
  if (getter && resolveInitialization) {
    resolveInitialization();
    resolveInitialization = null;
    initializationPromise = null;
  }
};

export const getToken = async (): Promise<string | undefined> => {
  if (!tokenGetter) {
    if (!initializationPromise) {
      initializationPromise = new Promise((resolve) => {
        resolveInitialization = resolve;
      });
    }
    // Wait up to 2 seconds for initialization to avoid infinite hangs in tests
    await Promise.race([
      initializationPromise,
      new Promise((_, reject) => setTimeout(() => reject(new Error('Token getter initialization timeout')), 2000))
    ]).catch(() => {
      // Do nothing, proceed with undefined
    });
  }
  
  if (tokenGetter) {
    return tokenGetter();
  }
  return undefined;
};

// For testing purposes and logout
export const resetTokenState = () => {
  tokenGetter = null;
  initializationPromise = null;
  resolveInitialization = null;
};
