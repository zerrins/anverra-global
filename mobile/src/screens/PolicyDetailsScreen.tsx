import React from 'react';
import { View, Text, StyleSheet, ActivityIndicator, ScrollView, RefreshControl } from 'react-native';
import { useRoute, RouteProp } from '@react-navigation/native';
import { useGetPolicy } from '../api/generated/endpoints';
import { PoliciesStackParamList } from '../navigation/RootNavigator';
import { useTheme } from '../theme/ThemeProvider';

type PolicyDetailsRouteProp = RouteProp<PoliciesStackParamList, 'PolicyDetails'>;

export function PolicyDetailsScreen() {
  const theme = useTheme();
  const route = useRoute<PolicyDetailsRouteProp>();
  const { policyId } = route.params;

  const { data, isLoading, isError, error, refetch, isRefetching } = useGetPolicy(policyId);
  const isForbidden = (error as any)?.status === 403;
  const isNotFound = (error as any)?.status === 404;

  if (isForbidden) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.background }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.error, marginBottom: theme.spacing.md }]}>Access Denied</Text>
        <Text style={[theme.typography.body, { color: theme.colors.textSecondary, textAlign: 'center' }]}>
          You do not have permission to view this policy.
        </Text>
      </View>
    );
  }

  if (isNotFound) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.background }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.error, marginBottom: theme.spacing.md }]}>Policy Not Found</Text>
        <Text style={[theme.typography.body, { color: theme.colors.textSecondary, textAlign: 'center' }]}>
          The policy you are looking for does not exist.
        </Text>
      </View>
    );
  }

  if (isLoading && !isRefetching) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.surface }]}>
        <ActivityIndicator size="large" color={theme.colors.primary} accessibilityLabel="Loading policy details" />
      </View>
    );
  }

  if (isError) {
    return (
      <View style={[styles.centered, { backgroundColor: theme.colors.surface }]}>
        <Text style={[theme.typography.body, { color: theme.colors.error, marginBottom: theme.spacing.md }]}>Failed to load policy details.</Text>
      </View>
    );
  }

  const policy = data?.data;
  if (!policy) return null;

  return (
    <ScrollView 
      style={[styles.container, { backgroundColor: theme.colors.surface }]}
      refreshControl={<RefreshControl refreshing={isRefetching} onRefresh={refetch} tintColor={theme.colors.primary} />}
    >
      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Policy Number</Text>
        <Text style={[theme.typography.h2, { color: theme.colors.text }]}>{policy.policyNumber}</Text>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Status</Text>
        <View style={[styles.statusBadge, { backgroundColor: theme.colors.surface }]}>
          <Text style={[theme.typography.body, { color: theme.colors.text }]}>{policy.status}</Text>
        </View>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.text, marginBottom: theme.spacing.md }]}>Coverage & Financials</Text>
        
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Premium</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text, marginBottom: theme.spacing.sm }]}>${policy.premium}</Text>
        
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Sum Assured</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text }]}>${policy.sumAssured}</Text>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.text, marginBottom: theme.spacing.md }]}>Dates</Text>
        
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Effective Date</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text, marginBottom: theme.spacing.sm }]}>{policy.effectiveDate}</Text>
        
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Expiry Date</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text }]}>{policy.expiryDate}</Text>
      </View>

      <View style={[styles.section, { backgroundColor: theme.colors.background, borderBottomColor: theme.colors.border }]}>
        <Text style={[theme.typography.h2, { color: theme.colors.text, marginBottom: theme.spacing.md }]}>Associations</Text>
        
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Customer ID</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text, marginBottom: theme.spacing.sm }]}>{policy.customerId}</Text>

        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Product ID</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text, marginBottom: theme.spacing.sm }]}>{policy.productId}</Text>
        
        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Insurer ID</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text, marginBottom: theme.spacing.sm }]}>{policy.insurerId}</Text>

        <Text style={[styles.label, theme.typography.caption, { color: theme.colors.textSecondary }]}>Branch ID</Text>
        <Text style={[theme.typography.body, { color: theme.colors.text }]}>{policy.branchId}</Text>
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  centered: { flex: 1, justifyContent: 'center', alignItems: 'center', padding: 24 },
  section: { padding: 16, borderBottomWidth: 1 },
  label: { textTransform: 'uppercase', marginBottom: 4 },
  statusBadge: { alignSelf: 'flex-start', paddingHorizontal: 8, paddingVertical: 4, borderRadius: 4 },
});
