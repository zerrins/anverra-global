let getAccessTokenFn: () => Promise<string | undefined> = async () => undefined;
let onUnauthorizedFn: () => void = () => {};

export const setAccessTokenProvider = (fn: () => Promise<string | undefined>) => {
  getAccessTokenFn = fn;
};

export const setOnUnauthorized = (fn: () => void) => {
  onUnauthorizedFn = fn;
};

export const getAccessToken = async (): Promise<string | undefined> => {
  return await getAccessTokenFn();
};

export const handleUnauthorized = () => {
  onUnauthorizedFn();
};
