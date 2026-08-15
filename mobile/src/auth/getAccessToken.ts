let getAccessTokenFn: () => Promise<string | undefined> = async () => undefined;

export const setAccessTokenProvider = (fn: () => Promise<string | undefined>) => {
  getAccessTokenFn = fn;
};

export const getAccessToken = async (): Promise<string | undefined> => {
  return await getAccessTokenFn();
};
